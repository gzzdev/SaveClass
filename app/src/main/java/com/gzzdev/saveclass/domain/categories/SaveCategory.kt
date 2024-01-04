package com.gzzdev.saveclass.domain.categories

import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.data.repository.CategoryRepository

class SaveCategory(private val repo: CategoryRepository){
    suspend operator fun invoke(category: Category) = repo.saveCategory(category)
}