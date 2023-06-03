package com.gzzdev.saveclass.ui.view.categories

import androidx.lifecycle.*
import com.gzzdev.saveclass.data.model.CategoriesTotalNotes
import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.domain.GetCategoriesTotalNotes
import com.gzzdev.saveclass.domain.RemoveCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesVM(
    getCategoriesTotalNotesUC: GetCategoriesTotalNotes,
    private val removeCategoryUC: RemoveCategory
): ViewModel() {
    val categories: LiveData<List<CategoriesTotalNotes>> = getCategoriesTotalNotesUC().asLiveData()

    fun removeCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        removeCategoryUC(category)
    }
}