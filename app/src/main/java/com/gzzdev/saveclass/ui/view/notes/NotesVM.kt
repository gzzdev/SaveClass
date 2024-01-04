package com.gzzdev.saveclass.ui.view.notes

import android.net.Uri
import androidx.lifecycle.*
import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.data.model.NoteWithCategory
import com.gzzdev.saveclass.domain.notes.GetNotes
import com.gzzdev.saveclass.domain.notes.RemoveNote
import com.gzzdev.saveclass.domain.notes.SaveNote
import com.gzzdev.saveclass.domain.notes.UpdateNote
import com.gzzdev.saveclass.ui.common.fileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class NotesVM(
    getNotesUC: GetNotes,
    private val saveNoteUC: SaveNote,
    private val updateNoteUC: UpdateNote,
    private val deleteNoteUC: RemoveNote
) : ViewModel() {
    val notes: LiveData<List<NoteWithCategory>> = getNotesUC(-1).asLiveData()

    private val _attachments = MutableLiveData(emptyList<Uri>())
    val attachments: LiveData<List<Uri>> get() = _attachments
    fun saveFastNote(title: String, text: String) {
        val newNote =
            Note(1, title, text, Date(), Date(), attachments.value!!.map { it.fileName() })
        viewModelScope.launch(Dispatchers.IO) {
            saveNoteUC(note = newNote)
        }
    }

    fun onFavoriteClick(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            updateNoteUC(note.copy(isFavorite = !note.isFavorite))
        }
    }

    fun onUnpinClick(note: Note) {
        viewModelScope.launch(Dispatchers.IO) { updateNoteUC(note.copy(isPin = false)) }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) { updateNoteUC(note) }
    }

    fun removeNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) { removeNote(note) }
    }

    fun addAttachment(uri: Uri) {
        _attachments.value = _attachments.value?.plus(uri)
    }

    fun removeAttachment(uri: Uri) {
        _attachments.value = _attachments.value?.minus(uri)
    }
}