package com.gzzdev.saveclass.data.source

import com.gzzdev.saveclass.data.model.CategoriesTotalNotes
import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.data.model.NoteWithCategory
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun saveCategory(category: Category): Long
    fun removeCategory(category: Category)
    fun getCategories(): Flow<List<Category>>
    fun getTotalCategories(): Flow<Int>
    fun getCategoriesTotalNotes(): Flow<List<CategoriesTotalNotes>>


    suspend fun saveNote(note: Note): Long
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun getNotes(): Flow<List<NoteWithCategory>>
    fun getNotesByCategory(categoryId: Int): Flow<List<NoteWithCategory>>
    fun getTotalNotes(): Flow<Int>
}