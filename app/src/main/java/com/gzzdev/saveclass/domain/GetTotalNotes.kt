package com.gzzdev.saveclass.domain

import com.gzzdev.saveclass.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetTotalNotes(private val repo: NoteRepository) {
    operator fun invoke(): Flow<Int> = repo.getTotalNotes()
}