package com.federicocotogno.minimalnotes.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.federicocotogno.minimalnotes.data.Note
import com.federicocotogno.minimalnotes.data.NoteDatabase
import com.federicocotogno.minimalnotes.data.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository
    val getAllData: LiveData<List<Note>>
    val orderDataByTitle: LiveData<List<Note>>

    val currentBoolean: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    init {
        val noteDao = NoteDatabase.getDatabase(
            application
        ).noteDao()
        repository =
            NoteRepository(noteDao)

        getAllData = repository.getAllData

        orderDataByTitle = repository.orderDataByTitle
    }

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllNotes()
        }
    }

}