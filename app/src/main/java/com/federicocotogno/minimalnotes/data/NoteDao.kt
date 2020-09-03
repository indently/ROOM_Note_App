package com.federicocotogno.minimalnotes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Note)

    @Query("SELECT * FROM note_table ORDER BY id DESC")
    fun getAllData(): LiveData<List<Note>>

    @Query("SELECT * FROM note_table ORDER BY LOWER(title) ASC")
    fun orderDataByTitle(): LiveData<List<Note>>

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

}