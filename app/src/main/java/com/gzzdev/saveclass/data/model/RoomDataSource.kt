package com.gzzdev.saveclass.data.model

import com.gzzdev.saveclass.data.SaveClassDb
import com.gzzdev.saveclass.data.source.LocalDataSource
import kotlinx.coroutines.flow.Flow

class RoomDataSource(db: SaveClassDb): LocalDataSource {
    private val categoryDao = db.categoryDao()
    private val noteDao = db.noteDao()
    override fun getCategories(): Flow<List<Category>> = categoryDao.getCategories()
    override fun getTotalCategories(): Flow<Int> = categoryDao.getTotalCategories()
    override suspend fun saveCategory(category: Category): Long = categoryDao.insert(category)
    override fun removeCategory(category: Category) = categoryDao.remove(category)
    override fun getCategoriesTotalNotes(): Flow<List<CategoriesTotalNotes>> = categoryDao
        .getCategoriesTotalNotes()

    override fun getNotes(): Flow<List<NoteWithCategory>> = noteDao.getNotes()
    override suspend fun updateNote(note: Note) { noteDao.insert(note) }
    override suspend fun saveNote(note: Note): Long = noteDao.insert(note)
    override fun getTotalNotes(): Flow<Int> = noteDao.getTotalNotes()
}