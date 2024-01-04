package com.gzzdev.saveclass.domain.notes

import com.gzzdev.saveclass.data.model.NoteWithCategory
import com.gzzdev.saveclass.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotes(private val repo: NoteRepository) {
    operator fun invoke(categoryId: Int): Flow<List<NoteWithCategory>> = repo.getNotes(categoryId)
}