package com.gzzdev.saveclass.domain

import com.gzzdev.saveclass.data.model.NoteWithCategory
import com.gzzdev.saveclass.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotes(private val repo: NoteRepository) {
    operator fun invoke(): Flow<List<NoteWithCategory>> = repo.getNotes()
}