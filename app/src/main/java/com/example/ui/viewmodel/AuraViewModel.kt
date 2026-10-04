package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.engine.LoadsheddingLookupEngine
import com.example.data.engine.LocalAuraEngine
import com.example.data.engine.ProverbItem
import com.example.data.engine.RecipeItem
import com.example.data.engine.SlangItem
import com.example.data.local.AuraDatabase
import com.example.data.local.AuraRepository
import com.example.data.model.AiTone
import com.example.data.model.AreaStatus
import com.example.data.model.ChatMessageEntity
import com.example.data.model.LoadsheddingArea
import com.example.data.model.SavedWisdomEntity
import com.example.data.model.VibeEntryEntity
import com.example.data.notification.LoadsheddingNotificationManager
import com.example.data.remote.GeminiApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class AuraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AuraRepository
    private val geminiClient = GeminiApiClient()
    private val prefs = application.getSharedPreferences("aura_prefs", Context.MODE_PRIVATE)

    private var tts: TextToSpeech? = null
    private val _isTtsReady = MutableStateFlow(false)
    val isTtsReady: StateFlow<Boolean> = _isTtsReady.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _isThinking = MutableStateFlow(false)
    val isThinking: StateFlow<Boolean> = _isThinking.asStateFlow()

    private val _aiTone = MutableStateFlow(
        AiTone.fromId(prefs.getString("pref_ai_tone", AiTone.MZANSI_CASUAL.id))
    )
    val aiTone: StateFlow<AiTone> = _aiTone.asStateFlow()

    private val _currentPersona = MutableStateFlow(
        prefs.getString("pref_persona", "MZANSI") ?: "MZANSI"
    ) // MZANSI, UBUNTU, KASI, LINGO
    val currentPersona: StateFlow<String> = _currentPersona.asStateFlow()

    private val _selectedProvince = MutableStateFlow(
        prefs.getString("pref_province", "Gauteng") ?: "Gauteng"
    )
    val selectedProvince: StateFlow<String> = _selectedProvince.asStateFlow()

    private val _activeLoadsheddingStage = MutableStateFlow(2)
    val activeLoadsheddingStage: StateFlow<Int> = _activeLoadsheddingStage.asStateFlow()

    private val _selectedArea = MutableStateFlow(LoadsheddingLookupEngine.allAreas[0])
    val selectedArea: StateFlow<LoadsheddingArea> = _selectedArea.asStateFlow()

    private val _bookmarkedAreaIds = MutableStateFlow(setOf("jhb_sandton", "cpt_city"))
    val bookmarkedAreaIds: StateFlow<Set<String>> = _bookmarkedAreaIds.asStateFlow()

    private val _searchSuburbsQuery = MutableStateFlow("")
    val searchSuburbsQuery: StateFlow<String> = _searchSuburbsQuery.asStateFlow()

    private val _is30MinAlertEnabled = MutableStateFlow(true)
    val is30MinAlertEnabled: StateFlow<Boolean> = _is30MinAlertEnabled.asStateFlow()

    private val _loadsheddingChecklist = MutableStateFlow(
        listOf(
            "Charge mobile phones & power banks" to true,
            "Boil water & fill thermos flask for coffee/tea" to false,
            "Unplug sensitive electronic devices from wall sockets" to true,
            "Check rechargeable emergency lights & flashlights" to false,
            "Download offline maps, study notes or music" to false,
            "Prep gas stove or braai coals for dinner" to false
        )
    )
    val loadsheddingChecklist: StateFlow<List<Pair<String, Boolean>>> = _loadsheddingChecklist.asStateFlow()

    val chatMessages: StateFlow<List<ChatMessageEntity>>
    val vibeHistory: StateFlow<List<VibeEntryEntity>>
    val savedWisdom: StateFlow<List<SavedWisdomEntity>>

    val isGeminiLive: Boolean get() = geminiClient.isApiKeyConfigured()

    init {
        val database = AuraDatabase.getDatabase(application)
        repository = AuraRepository(database)

        chatMessages = repository.allMessages.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        vibeHistory = repository.allVibes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        savedWisdom = repository.allSavedWisdom.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        initTts(application)
        seedWelcomeMessageIfNeeded()
        LoadsheddingNotificationManager.createNotificationChannel(application)
        if (_is30MinAlertEnabled.value) {
            LoadsheddingNotificationManager.schedule30MinAlert(application, _selectedArea.value, _activeLoadsheddingStage.value)
        }
    }

    private fun initTts(context: Application) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Try South African English locale or standard English
                val saLocale = Locale("en", "ZA")
                val available = tts?.isLanguageAvailable(saLocale)
                if (available == TextToSpeech.LANG_AVAILABLE || available == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                    tts?.language = saLocale
                } else {
                    tts?.language = Locale.ENGLISH
                }
                tts?.setSpeechRate(0.95f)
                _isTtsReady.value = true
            }
        }
    }

    private fun seedWelcomeMessageIfNeeded() {
        viewModelScope.launch {
            val count = AuraDatabase.getDatabase(getApplication()).chatDao().getMessageCount()
            if (count == 0) {
                val welcome = if (_aiTone.value == AiTone.FORMAL_ENGLISH) {
                    "Good day. I am Aura, your South African AI companion. I am at your service to assist with municipal loadshedding schedules, languages, local cuisine, and everyday inquiries in formal English. How may I assist you today?"
                } else {
                    "Howzit! I'm Aura. You can chat with me in English, Zulu, Sepedi, Afrikaans, or mix it up with Mzansi slang. Type or tap the mic!"
                }
                repository.saveMessage(
                    sender = "AURA",
                    text = welcome,
                    persona = _currentPersona.value
                )
            }
        }
    }

    fun setTone(tone: AiTone) {
        _aiTone.value = tone
        prefs.edit().putString("pref_ai_tone", tone.id).apply()
    }

    fun toggleTone() {
        val nextTone = if (_aiTone.value == AiTone.FORMAL_ENGLISH) AiTone.MZANSI_CASUAL else AiTone.FORMAL_ENGLISH
        setTone(nextTone)
    }

    fun setPersona(persona: String) {
        _currentPersona.value = persona
        prefs.edit().putString("pref_persona", persona).apply()
    }

    fun setProvince(province: String) {
        _selectedProvince.value = province
        prefs.edit().putString("pref_province", province).apply()
    }

    fun setLoadsheddingStage(stage: Int) {
        _activeLoadsheddingStage.value = stage
        if (_is30MinAlertEnabled.value) {
            LoadsheddingNotificationManager.schedule30MinAlert(getApplication(), _selectedArea.value, stage)
        }
    }

    fun toggleChecklistItem(index: Int) {
        val current = _loadsheddingChecklist.value.toMutableList()
        if (index in current.indices) {
            val item = current[index]
            current[index] = item.first to !item.second
            _loadsheddingChecklist.value = current
        }
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return
        val currentList = chatMessages.value
        val history = currentList.takeLast(6).map { it.sender to it.text }
        val persona = _currentPersona.value
        val province = _selectedProvince.value
        val tone = _aiTone.value

        viewModelScope.launch {
            // Save user message
            repository.saveMessage(sender = "USER", text = userText.trim(), persona = persona)

            _isThinking.value = true
            try {
                val auraResponse = geminiClient.generateAuraResponse(
                    userPrompt = userText.trim(),
                    persona = persona,
                    tone = tone,
                    province = province,
                    conversationHistory = history
                )
                repository.saveMessage(sender = "AURA", text = auraResponse, persona = persona)
            } finally {
                _isThinking.value = false
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
            seedWelcomeMessageIfNeeded()
        }
    }

    fun speakText(text: String) {
        if (!_isTtsReady.value || tts == null) return
        if (tts?.isSpeaking == true) {
            tts?.stop()
            _isSpeaking.value = false
            return
        }
        // Remove markdown formatting characters for natural voice
        val cleanText = text.replace("*", "").replace("#", "").replace("•", "")
        _isSpeaking.value = true
        tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "aura_utterance")
    }

    fun stopSpeaking() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun logVibe(vibeType: String, note: String) {
        viewModelScope.launch {
            val response = when (vibeType) {
                "LEKKER" -> "Lekker! Glad things are feeling good. Keep that positive momentum going today."
                "SHARP" -> "Sharp sharp! Steady, grounded, and moving forward smoothly."
                "UBUNTU" -> "Umuntu ngumuntu ngabantu. Staying connected and mindful of those around us keeps our spirits resilient."
                "Eish" -> "Eish, sorry you had a tough stretch today. South African life has its challenges, but you're resilient. Take time to unwind tonight."
                "LOADSHEDDING" -> "Loadshedding is definitely frustrating. Keep devices charged, get some hot water in a flask, and don't let Eskom ruin your peace."
                else -> "Aura is here with you. Whatever comes your way, take it one step at a time."
            }
            repository.saveVibe(vibeType, note, response)
        }
    }

    fun deleteVibe(id: Long) {
        viewModelScope.launch {
            repository.deleteVibe(id)
        }
    }

    fun saveProverb(proverb: ProverbItem) {
        viewModelScope.launch {
            repository.saveWisdom(
                category = "PROVERB",
                title = "${proverb.language}: ${proverb.originalText}",
                subtitle = proverb.englishTranslation,
                content = proverb.lifeMeaning
            )
        }
    }

    fun saveSlang(slang: SlangItem) {
        viewModelScope.launch {
            repository.saveWisdom(
                category = "SLANG",
                title = slang.word,
                subtitle = slang.meaning,
                content = "Example: ${slang.exampleSentence}"
            )
        }
    }

    fun saveRecipe(recipe: RecipeItem) {
        viewModelScope.launch {
            repository.saveWisdom(
                category = "RECIPE",
                title = recipe.name,
                subtitle = "${recipe.region} (${recipe.prepTime})",
                content = "${recipe.description}\n\nTip: ${recipe.auraTip}"
            )
        }
    }

    fun deleteSavedWisdom(id: Long) {
        viewModelScope.launch {
            repository.deleteSavedWisdom(id)
        }
    }

    fun selectArea(area: LoadsheddingArea) {
        _selectedArea.value = area
        if (_is30MinAlertEnabled.value) {
            LoadsheddingNotificationManager.schedule30MinAlert(getApplication(), area, _activeLoadsheddingStage.value)
        }
    }

    fun toggle30MinAlert(enabled: Boolean) {
        _is30MinAlertEnabled.value = enabled
        if (enabled) {
            LoadsheddingNotificationManager.schedule30MinAlert(getApplication(), _selectedArea.value, _activeLoadsheddingStage.value)
        } else {
            LoadsheddingNotificationManager.cancelAlerts(getApplication())
        }
    }

    fun triggerTestNotification() {
        LoadsheddingNotificationManager.showOutageAlertNotification(
            context = getApplication(),
            suburb = _selectedArea.value.suburb,
            municipality = _selectedArea.value.municipality,
            startTime = "16:00",
            endTime = "18:30",
            stage = _activeLoadsheddingStage.value
        )
    }

    fun toggleBookmarkArea(areaId: String) {
        val current = _bookmarkedAreaIds.value.toMutableSet()
        if (current.contains(areaId)) {
            current.remove(areaId)
        } else {
            current.add(areaId)
        }
        _bookmarkedAreaIds.value = current
    }

    fun setSearchSuburbsQuery(query: String) {
        _searchSuburbsQuery.value = query
    }

    fun getFilteredAreas(query: String): List<LoadsheddingArea> {
        val q = query.trim().lowercase()
        return if (q.isEmpty()) {
            LoadsheddingLookupEngine.allAreas
        } else {
            LoadsheddingLookupEngine.allAreas.filter {
                it.suburb.lowercase().contains(q) ||
                it.municipality.lowercase().contains(q) ||
                it.province.lowercase().contains(q)
            }
        }
    }

    fun getAreaStatus(area: LoadsheddingArea, stage: Int): AreaStatus {
        return LoadsheddingLookupEngine.calculateAreaStatus(area, stage)
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
