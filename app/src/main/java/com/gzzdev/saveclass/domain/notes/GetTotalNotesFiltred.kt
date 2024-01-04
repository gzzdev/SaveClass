package com.gzzdev.saveclass.domain.notes

import com.gzzdev.saveclass.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetTotalNotesFiltred(private val repo: NoteRepository) {
    operator fun invoke(): Flow<Int> = repo.getTotalNotes()
}