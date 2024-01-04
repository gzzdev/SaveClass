package com.gzzdev.saveclass.domain.notes

import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.data.repository.NoteRepository

class RemoveNote(private val repo: NoteRepository){
    suspend operator fun invoke(note: Note) = repo.deleteNote(note)
}