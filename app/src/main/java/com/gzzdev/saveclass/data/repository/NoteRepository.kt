package com.gzzdev.saveclass.data.repository

import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.data.source.LocalDataSource

class NoteRepository(private val localDataSource: LocalDataSource){
    suspend fun saveNote(note: Note) = localDataSource.saveNote(note)
    suspend fun updateNote(note: Note) = localDataSource.updateNote(note)
    fun getNotes() = localDataSource.getNotes()
    fun getTotalNotes() = localDataSource.getTotalNotes()
}