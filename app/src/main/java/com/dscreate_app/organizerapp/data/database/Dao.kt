package com.dscreate_app.organizerapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dscreate_app.organizerapp.data.entities.NoteItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM note_item")
    fun getAllNotes(): Flow<List<NoteItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteItemEntity)

    @Query("DELETE FROM note_item WHERE id IS :id")
    suspend fun deleteNote(id: Int)
}