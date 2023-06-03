package com.gzzdev.saveclass.ui.view.notes

import androidx.lifecycle.*
import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.data.model.NoteWithCategory
import com.gzzdev.saveclass.domain.GetNotes
import com.gzzdev.saveclass.domain.SaveNote
import com.gzzdev.saveclass.domain.UpdateNote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class NotesVM(
    getNotesUC: GetNotes,
    private val saveNoteUC: SaveNote,
    private val updateNoteUC: UpdateNote
): ViewModel() {

    val notes: LiveData<List<NoteWithCategory>> = getNotesUC().asLiveData()

    fun saveFastNote(title: String, text: String, imagePaths: ArrayList<String>) {
        val newNote = Note(1, title, text, Date(), Date(), imagePaths)
        viewModelScope.launch(Dispatchers.IO) {
            saveNoteUC(note = newNote)
        }
    }

    fun onFavoriteClick(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            updateNoteUC(note.copy(isFavorite = !note.isFavorite))
        }
    }

    fun onPinClick(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            updateNoteUC(note.copy(isPin = !note.isPin))
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            updateNoteUC(note)
        }
    }

}