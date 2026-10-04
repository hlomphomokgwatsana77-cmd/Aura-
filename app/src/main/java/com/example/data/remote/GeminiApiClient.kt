package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.engine.LocalAuraEngine
import com.example.data.model.AiTone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiApiClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    fun isApiKeyConfigured(): Boolean {
        return try {
            val key = BuildConfig.GEMINI_API_KEY
            key.isNotBlank() && key != "MY_GEMINI_API_KEY"
        } catch (e: Throwable) {
            false
        }
    }

    suspend fun generateAuraResponse(
        userPrompt: String,
        persona: String,
        tone: AiTone = AiTone.MZANSI_CASUAL,
        province: String = "Gauteng",
        conversationHistory: List<Pair<String, String>> = emptyList()
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Throwable) { "" }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Local high-fidelity Mzansi engine fallback
            return@withContext LocalAuraEngine.generateLocalResponse(userPrompt, persona, tone)
        }

        try {
            val toneInstruction = if (tone == AiTone.FORMAL_ENGLISH) {
                """
                TONE & REGISTER DIRECTIVE: STRICT FORMAL ENGLISH MODE.
                - Express all thoughts in polished, articulate, professional standard English.
                - Maintain refined grammar, courteous etiquette, and an elevated yet accessible register.
                - Do NOT use colloquial South African slang or informal slang terms (avoid 'howzit', 'lekker', 'eish', 'sharp sharp', 'sho', 'yebo', 'bra', etc., unless the user explicitly requests an explanation or translation of a slang term).
                - Respect South African cultural heritage and regional context, but communicate with standard, dignified professionalism.
                """.trimIndent()
            } else {
                """
                TONE & REGISTER DIRECTIVE: CASUAL MZANSI SLANG STYLE.
                - Speak in a relaxed, friendly, upbeat South African voice seasoned with authentic everyday Mzansi slang (Howzit, Lekker, Eish, Sharp sharp, Sho, Yebo, No stress, Braai, etc.).
                - Be relatable, warm, witty, and streetwise like an authentic South African companion.
                - Mix in natural South African colloquialisms and phrasing smoothly.
                """.trimIndent()
            }

            val systemContext = "You are Aura, an empathetic, culturally attuned AI companion designed specifically for South Africa. Aesthetic: sleek dark purple. $toneInstruction Companion persona focus: $persona. The user is in or interested in the $province province. Keep responses practical, relatable, and culturally attuned."

            // Build payload
            val root = JSONObject()

            // System instruction
            val systemInstructionObj = JSONObject().apply {
                put("parts", JSONArray().apply {
                    put(JSONObject().apply {
                        put("text", systemContext)
                    })
                })
            }
            root.put("systemInstruction", systemInstructionObj)

            // Contents array
            val contentsArray = JSONArray()
            val recentTurns = conversationHistory.takeLast(6)
            for (turn in recentTurns) {
                val role = if (turn.first.equals("USER", ignoreCase = true)) "user" else "model"
                contentsArray.put(JSONObject().apply {
                    put("role", role)
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", turn.second)
                        })
                    })
                })
            }

            // Current prompt
            contentsArray.put(JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().apply {
                    put(JSONObject().apply {
                        put("text", userPrompt)
                    })
                })
            })
            root.put("contents", contentsArray)

            // Generation config
            val genConfig = JSONObject().apply {
                put("temperature", if (tone == AiTone.FORMAL_ENGLISH) 0.5 else 0.75)
                put("topP", 0.95)
                put("maxOutputTokens", 1024)
            }
            root.put("generationConfig", genConfig)

            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val requestBody = root.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                val errorBody = response.body?.string() ?: ""
                Log.w("GeminiApiClient", "API error: ${response.code} $errorBody")
                return@withContext LocalAuraEngine.generateLocalResponse(userPrompt, persona, tone)
            }

            val responseBody = response.body?.string() ?: ""
            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val text = parts.getJSONObject(0).optString("text")
                    if (text.isNotBlank()) {
                        return@withContext text.trim()
                    }
                }
            }

            LocalAuraEngine.generateLocalResponse(userPrompt, persona, tone)
        } catch (e: Exception) {
            Log.e("GeminiApiClient", "Call failure, falling back to local engine", e)
            LocalAuraEngine.generateLocalResponse(userPrompt, persona, tone)
        }
    }
}
