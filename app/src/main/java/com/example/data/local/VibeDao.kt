package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.VibeEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VibeDao {
    @Query("SELECT * FROM vibe_entries ORDER BY timestamp DESC")
    fun getAllVibes(): Flow<List<VibeEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVibe(vibe: VibeEntryEntity): Long

    @Query("DELETE FROM vibe_entries WHERE id = :id")
    suspend fun deleteVibe(id: Long)
}
