package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sender: String, // "USER" or "AURA"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val persona: String = "MZANSI", // "MZANSI", "UBUNTU", "KASI", "LINGO"
    val isTtsAvailable: Boolean = true
)
