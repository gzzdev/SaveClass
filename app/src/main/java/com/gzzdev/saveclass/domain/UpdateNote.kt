package com.gzzdev.saveclass.domain

import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.data.repository.NoteRepository


class UpdateNote(private val repo: NoteRepository){
    suspend operator fun invoke(note: Note) = repo.updateNote(note)
}