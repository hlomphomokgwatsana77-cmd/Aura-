package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vibe_entries")
data class VibeEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vibeType: String, // "LEKKER", "SHARP", "EISH", "UBUNTU", "LOADSHEDDING"
    val userNote: String,
    val auraResponse: String,
    val timestamp: Long = System.currentTimeMillis()
)
