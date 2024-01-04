package com.gzzdev.saveclass.ui.view.new_category_bottom_sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.data.model.ColorCategory
import com.gzzdev.saveclass.domain.categories.SaveCategory
import com.gzzdev.saveclass.ui.common.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewCategoryVM(private val saveCategoryUC: SaveCategory): ViewModel() {
    private val _hide = MutableLiveData(false)
    val hide: LiveData<Boolean> get() = _hide
    private var checkedColor = 0

    private val _colors = MutableLiveData(
        Utils.PASTEL_COLOURS.flatMap { list -> list.map { ColorCategory(it, false) } }
    )
    val colors: LiveData<List<ColorCategory>> get() = _colors
    fun checkColor(color: ColorCategory) {
        _colors.value = _colors.value!!.map {
            if (it.color == color.color) it.copy(checked = true)
            else it.copy(checked = false)
        }
        checkedColor = color.color
    }
    fun saveCategory(name: String) {
        val newCategory = Category(name.trim(), checkedColor)
        viewModelScope.launch(Dispatchers.IO) {
            saveCategoryUC(category = newCategory)
            _hide.postValue(true)
        }
    }
}