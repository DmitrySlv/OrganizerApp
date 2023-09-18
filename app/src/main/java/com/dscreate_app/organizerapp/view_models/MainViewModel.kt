package com.dscreate_app.organizerapp.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dscreate_app.organizerapp.data.database.OrganizerDb
import com.dscreate_app.organizerapp.data.entities.NoteItemEntity
import kotlinx.coroutines.launch

class MainViewModel(database: OrganizerDb): ViewModel() {
    private val dao = database.getDao()
    val allNotes: LiveData<List<NoteItemEntity>> = dao.getAllNotes().asLiveData()

    fun insertNote(note: NoteItemEntity) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun updateNote(note: NoteItemEntity) = viewModelScope.launch {
        dao.updateNote(note)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }
}