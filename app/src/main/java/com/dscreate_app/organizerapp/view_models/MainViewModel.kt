package com.dscreate_app.organizerapp.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dscreate_app.organizerapp.data.database.OrganizerDb
import com.dscreate_app.organizerapp.data.entities.LibraryItemEntity
import com.dscreate_app.organizerapp.data.entities.NoteItemEntity
import com.dscreate_app.organizerapp.data.entities.ShoppingListItemEntity
import com.dscreate_app.organizerapp.data.entities.ShoppingListNameEntity
import kotlinx.coroutines.launch

class MainViewModel(database: OrganizerDb): ViewModel() {

    private val dao = database.getDao()

    val allNotes: LiveData<List<NoteItemEntity>> = dao.getAllNotes().asLiveData()

    val allShoppingListNames: LiveData<List<ShoppingListNameEntity>> =
        dao.getAllShoppingListNames().asLiveData()

    val libraryItems = MutableLiveData<List<LibraryItemEntity>>()

    fun getAllShoppingListItems(listId: Int): LiveData<List<ShoppingListItemEntity>> {
        return dao.getAllShoppingListItems(listId).asLiveData()
    }

    fun getAllLibraryItems(name: String) = viewModelScope.launch {
       libraryItems.postValue(dao.getAllLibraryItems(name))
    }

    fun insertNote(note: NoteItemEntity) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertShoppingListName(shoppingListName: ShoppingListNameEntity) = viewModelScope.launch {
        dao.insertShoppingListName(shoppingListName)
    }

    fun insertShoppingListItem(shoppingListItem: ShoppingListItemEntity) = viewModelScope.launch {
        dao.insertShoppingListItem(shoppingListItem)
        if (!isLibraryItemExists(shoppingListItem.name)) {
            dao.insertLibraryItem(LibraryItemEntity(null, shoppingListItem.name, ""))
        }
    }

    fun updateNote(note: NoteItemEntity) = viewModelScope.launch {
        dao.updateNote(note)
    }

    fun updateShoppingListItem(shoppingListItem: ShoppingListItemEntity) = viewModelScope.launch {
        dao.updateShoppingListItem(shoppingListItem)
    }

    fun updateShoppingListName(shoppingListName: ShoppingListNameEntity) = viewModelScope.launch {
        dao.updateShoppingListName(shoppingListName)
    }

    fun updateLibraryItem(libraryItem: LibraryItemEntity) = viewModelScope.launch {
        dao.updateLibraryItem(libraryItem)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    fun deleteShoppingList(id: Int, deleteList: Boolean) = viewModelScope.launch {
        if (deleteList) {
            dao.deleteShoppingListNames(id)
        } else {
            dao.deleteShoppingListItemsByListId(id)
        }
    }

    fun deleteLibraryItem(id: Int) = viewModelScope.launch {
        dao.deleteLibraryItem(id)
    }

   private suspend fun isLibraryItemExists(name: String): Boolean {
        return dao.getAllLibraryItems(name).isNotEmpty()
    }
}