package com.gzzdev.saveclass.ui.view.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.gzzdev.saveclass.domain.categories.GetTotalCategories
import com.gzzdev.saveclass.domain.notes.GetTotalNotesFiltred

class SettingsVM(
    getTotalNotesFiltredUC: GetTotalNotesFiltred,
    getTotalCategoriesUC: GetTotalCategories
): ViewModel() {
    val totalNotes: LiveData<Int> = getTotalNotesFiltredUC().asLiveData()
    val totalCategories: LiveData<Int> = getTotalCategoriesUC().asLiveData()
}