package com.gzzdev.saveclass.ui.view.new_category_bottom_sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.domain.SaveCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewCategoryVM(private val saveCategoryUC: SaveCategory): ViewModel() {
    private val _hide = MutableLiveData(false)
    val hide: LiveData<Boolean> get() = _hide
    fun saveCategory(name: String){
        val newCategory = Category(name.trim(), 0)
        viewModelScope.launch(Dispatchers.IO) {
            saveCategoryUC(category = newCategory)
            _hide.postValue(true)
        }
    }
}