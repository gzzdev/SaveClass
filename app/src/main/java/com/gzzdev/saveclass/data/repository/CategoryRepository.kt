package com.gzzdev.saveclass.data.repository

import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.data.source.LocalDataSource

class CategoryRepository(private val localDataSource: LocalDataSource){
    suspend fun saveCategory(category: Category) = localDataSource.saveCategory(category)
    fun removeCategory(category: Category) = localDataSource.removeCategory(category)
    fun getCategories() = localDataSource.getCategories()
    fun getTotalCategories() = localDataSource.getTotalCategories()
    fun getCategoriesTotalNotes() = localDataSource.getCategoriesTotalNotes()
}