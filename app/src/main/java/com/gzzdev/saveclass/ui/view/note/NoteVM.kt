package com.gzzdev.saveclass.ui.view.note

import androidx.lifecycle.*
import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.domain.categories.GetCategories
import com.gzzdev.saveclass.domain.notes.SaveNote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class NoteVM(getCategoriesUC: GetCategories, private val saveNoteUC: SaveNote): ViewModel() {
    private val _hide = MutableLiveData(false)
    val hide: LiveData<Boolean> get() = _hide

    private lateinit var categorySelected: Category
    val categories: LiveData<List<Category>> = getCategoriesUC().asLiveData()

    /*private val _imagesPaths = MutableLiveData(mutableListOf<Uri>())
    val imagesPaths: LiveData<MutableList<Uri>> get() = _imagesPaths*/

    fun saveNote(title: String, text: String, currentList: ArrayList<String>){
        val newNote = Note(
            categorySelected.idCategory, title.trim(), text, Date(), Date(), currentList
        )
        viewModelScope.launch(Dispatchers.IO) {
            saveNoteUC(note = newNote)
            _hide.postValue(true)
        }
    }

    fun setCategorySelected(category: Category) { categorySelected = category }

    /*fun addImagePath(imagePath: Uri) {
        _imagesPaths.value = _imagesPaths.value!!.apply{ add(imagePath) }
    }*/
}