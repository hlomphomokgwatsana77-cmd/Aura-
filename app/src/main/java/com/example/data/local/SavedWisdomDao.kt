package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.SavedWisdomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedWisdomDao {
    @Query("SELECT * FROM saved_wisdom ORDER BY timestamp DESC")
    fun getAllSaved(): Flow<List<SavedWisdomEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaved(item: SavedWisdomEntity): Long

    @Query("DELETE FROM saved_wisdom WHERE id = :id")
    suspend fun deleteSaved(id: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_wisdom WHERE title = :title)")
    suspend fun isSaved(title: String): Boolean
}
