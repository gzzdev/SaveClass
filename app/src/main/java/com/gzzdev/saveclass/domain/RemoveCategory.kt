package com.gzzdev.saveclass.domain

import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.data.repository.CategoryRepository

class RemoveCategory(private val repo: CategoryRepository){
    operator fun invoke(category: Category) = repo.removeCategory(category)
}