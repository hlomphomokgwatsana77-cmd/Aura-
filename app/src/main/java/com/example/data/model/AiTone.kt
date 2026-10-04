package com.example.data.model

enum class AiTone(
    val id: String,
    val title: String,
    val subtitle: String,
    val badgeLabel: String,
    val sampleGreeting: String,
    val description: String
) {
    FORMAL_ENGLISH(
        id = "FORMAL_ENGLISH",
        title = "Formal English",
        subtitle = "Polite, articulate & professional standard English",
        badgeLabel = "Formal English",
        sampleGreeting = "Good day. I am Aura. How may I assist you with your schedule, translations, or inquiries today?",
        description = "Speaks in refined, standard formal English with courteous phrasing and clean grammar. Avoids informal colloquialisms and street slang."
    ),
    MZANSI_CASUAL(
        id = "MZANSI_CASUAL",
        title = "Casual 'Mzansi' Slang",
        subtitle = "Warm, relaxed & seasoned with South African slang",
        badgeLabel = "Mzansi Slang",
        sampleGreeting = "Howzit! Lekker to see you. Let me know what you need, sharp sharp!",
        description = "Conversational everyday South African style featuring authentic Mzansi slang (Howzit, Lekker, Eish, Sharp sharp, Sho, Yebo) and local culture."
    );

    companion object {
        fun fromId(id: String?): AiTone {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: MZANSI_CASUAL
        }
    }
}
