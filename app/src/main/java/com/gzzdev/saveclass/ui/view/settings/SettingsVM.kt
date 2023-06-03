package com.gzzdev.saveclass.ui.view.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.gzzdev.saveclass.data.model.NoteWithCategory
import com.gzzdev.saveclass.domain.GetTotalCategories
import com.gzzdev.saveclass.domain.GetTotalNotes

class SettingsVM(
    getTotalNotesUC: GetTotalNotes,
    getTotalCategoriesUC: GetTotalCategories
): ViewModel() {
    val totalNotes: LiveData<Int> = getTotalNotesUC().asLiveData()
    val totalCategories: LiveData<Int> = getTotalCategoriesUC().asLiveData()
}