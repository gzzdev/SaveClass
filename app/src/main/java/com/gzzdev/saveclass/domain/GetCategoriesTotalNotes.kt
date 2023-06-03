package com.gzzdev.saveclass.domain

import com.gzzdev.saveclass.data.model.CategoriesTotalNotes
import com.gzzdev.saveclass.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetCategoriesTotalNotes(private val repo: CategoryRepository) {
    operator fun invoke(): Flow<List<CategoriesTotalNotes>> = repo.getCategoriesTotalNotes()
}