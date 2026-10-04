package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_wisdom")
data class SavedWisdomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String, // "PROVERB", "SLANG", "RECIPE", "PREP_TIP"
    val title: String,
    val subtitle: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
