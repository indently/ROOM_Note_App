package com.federicocotogno.minimalnotes.data.repository

import androidx.lifecycle.LiveData
import com.federicocotogno.minimalnotes.data.Note
import com.federicocotogno.minimalnotes.data.NoteDao

class NoteRepository(private val noteDao: NoteDao) {
    val getAllData: LiveData<List<Note>> = noteDao.getAllData()
    val orderDataByTitle: LiveData<List<Note>> = noteDao.orderDataByTitle()

    suspend fun addNote(note: Note) {
        noteDao.addNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun deleteAllNotes() {
        noteDao.deleteAll()
    }

}