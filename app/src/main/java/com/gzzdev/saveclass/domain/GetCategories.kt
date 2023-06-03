package com.gzzdev.saveclass.domain

import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetCategories(private val repo: CategoryRepository) {
    operator fun invoke(): Flow<List<Category>> = repo.getCategories()
}