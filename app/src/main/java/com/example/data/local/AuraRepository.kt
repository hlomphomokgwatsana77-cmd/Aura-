package com.example.data.local

import com.example.data.model.ChatMessageEntity
import com.example.data.model.SavedWisdomEntity
import com.example.data.model.VibeEntryEntity
import kotlinx.coroutines.flow.Flow

class AuraRepository(private val database: AuraDatabase) {
    val allMessages: Flow<List<ChatMessageEntity>> = database.chatDao().getAllMessages()
    val allVibes: Flow<List<VibeEntryEntity>> = database.vibeDao().getAllVibes()
    val allSavedWisdom: Flow<List<SavedWisdomEntity>> = database.savedWisdomDao().getAllSaved()

    suspend fun saveMessage(sender: String, text: String, persona: String): Long {
        return database.chatDao().insertMessage(
            ChatMessageEntity(
                sender = sender,
                text = text,
                persona = persona
            )
        )
    }

    suspend fun clearChat() {
        database.chatDao().clearHistory()
    }

    suspend fun saveVibe(vibeType: String, userNote: String, auraResponse: String): Long {
        return database.vibeDao().insertVibe(
            VibeEntryEntity(
                vibeType = vibeType,
                userNote = userNote,
                auraResponse = auraResponse
            )
        )
    }

    suspend fun deleteVibe(id: Long) {
        database.vibeDao().deleteVibe(id)
    }

    suspend fun saveWisdom(category: String, title: String, subtitle: String, content: String): Long {
        return database.savedWisdomDao().insertSaved(
            SavedWisdomEntity(
                category = category,
                title = title,
                subtitle = subtitle,
                content = content
            )
        )
    }

    suspend fun deleteSavedWisdom(id: Long) {
        database.savedWisdomDao().deleteSaved(id)
    }

    suspend fun isWisdomSaved(title: String): Boolean {
        return database.savedWisdomDao().isSaved(title)
    }
}
