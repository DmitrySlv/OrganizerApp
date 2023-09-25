package com.dscreate_app.organizerapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dscreate_app.organizerapp.data.entities.LibraryItemEntity
import com.dscreate_app.organizerapp.data.entities.NoteItemEntity
import com.dscreate_app.organizerapp.data.entities.ShoppingListItemEntity
import com.dscreate_app.organizerapp.data.entities.ShoppingListNameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM shopping_list_names")
    fun getAllShoppingListNames(): Flow<List<ShoppingListNameEntity>>

    @Query("SELECT * FROM shopping_list_item WHERE listId LIKE :listId")
    fun getAllShoppingListItems(listId: Int): Flow<List<ShoppingListItemEntity>>

    @Query("SELECT * FROM library_item WHERE name LIKE :name")
    suspend fun getAllLibraryItems(name: String): List<LibraryItemEntity>

    @Query("SELECT * FROM note_item")
    fun getAllNotes(): Flow<List<NoteItemEntity>>

    @Query("DELETE FROM note_item WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Query("DELETE FROM shopping_list_names WHERE id IS :id")
    suspend fun deleteShoppingListNames(id: Int)

    @Query("DELETE FROM shopping_list_item WHERE listId LIKE :listId")
    suspend fun deleteShoppingListItemsByListId(listId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingListName(shoppingListName: ShoppingListNameEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingListItem(shoppingListItem: ShoppingListItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibraryItem(libraryItem: LibraryItemEntity)

    @Update
    suspend fun updateNote(note: NoteItemEntity)

    @Update
    suspend fun updateShoppingListName(shoppingListName: ShoppingListNameEntity)

    @Update
    suspend fun updateShoppingListItem(shoppingListItem: ShoppingListItemEntity)

}