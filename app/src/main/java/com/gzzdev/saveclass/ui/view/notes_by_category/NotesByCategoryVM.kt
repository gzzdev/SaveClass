package com.gzzdev.saveclass.ui.view.notes_by_category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.data.model.NoteWithCategory
import com.gzzdev.saveclass.domain.notes.GetNotes
import com.gzzdev.saveclass.domain.notes.SaveNote
import com.gzzdev.saveclass.domain.notes.UpdateNote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class NotesByCategoryVM(
    getNotesUC: GetNotes,
    private val saveNoteUC: SaveNote,
    private val updateNoteUC: UpdateNote,
    categoryId: Int
): ViewModel() {

    val notes: LiveData<List<NoteWithCategory>> = getNotesUC(categoryId).asLiveData()

    fun saveFastNote(title: String, text: String, imagePaths: ArrayList<String>) {
        val newNote = Note(1, title, text, Date(), Date(), imagePaths)
        viewModelScope.launch(Dispatchers.IO) { saveNoteUC(note = newNote) }
    }

    fun onFavoriteClick(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            updateNoteUC(note.copy(isFavorite = !note.isFavorite))
        }
    }

    fun onPinClick(note: Note) {
        viewModelScope.launch(Dispatchers.IO) { updateNoteUC(note.copy(isPin = !note.isPin)) }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) { updateNoteUC(note) }
    }

}