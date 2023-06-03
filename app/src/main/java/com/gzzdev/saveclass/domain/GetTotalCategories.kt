package com.gzzdev.saveclass.domain

import com.gzzdev.saveclass.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetTotalCategories(private val repo: CategoryRepository) {
    operator fun invoke(): Flow<Int> = repo.getTotalCategories()
}