package com.dscreate_app.organizerapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dscreate_app.organizerapp.data.entities.NoteItemEntity
import com.dscreate_app.organizerapp.data.entities.ShoppingListNameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM shopping_list_names")
    fun getAllShoppingListNames(): Flow<List<ShoppingListNameEntity>>

    @Query("SELECT * FROM note_item")
    fun getAllNotes(): Flow<List<NoteItemEntity>>

    @Query("DELETE FROM note_item WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopListName(shoppingListName: ShoppingListNameEntity)

    @Update
    suspend fun updateNote(note: NoteItemEntity)

}