package com.gzzdev.saveclass.ui.view.notes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.gzzdev.saveclass.R
import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.data.model.RoomDataSource
import com.gzzdev.saveclass.data.repository.NoteRepository
import com.gzzdev.saveclass.databinding.FragmentNotesBinding
import com.gzzdev.saveclass.domain.notes.GetNotes
import com.gzzdev.saveclass.domain.notes.RemoveNote
import com.gzzdev.saveclass.domain.notes.SaveNote
import com.gzzdev.saveclass.domain.notes.UpdateNote
import com.gzzdev.saveclass.ui.common.MultimediaManagerObserver
import com.gzzdev.saveclass.ui.common.NotesAdapter
import com.gzzdev.saveclass.ui.common.app
import com.gzzdev.saveclass.ui.common.fileName
import com.gzzdev.saveclass.ui.common.hideKeyboard
import com.gzzdev.saveclass.ui.common.showMessage
import java.io.File
import java.io.FileOutputStream

class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    lateinit var multimediaObserver: MultimediaManagerObserver
    private lateinit var notesVM: NotesVM
    private lateinit var notesAdapter: NotesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        multimediaObserver = MultimediaManagerObserver(
            binding.btnAddNote,
            requireContext()
        ) { uri -> notesVM.addAttachment(uri) }
        lifecycle.addObserver(multimediaObserver)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observers()
        listeners()
    }

    private fun setup() {
        val noteRepository = NoteRepository(RoomDataSource(requireContext().app.room))
        notesVM = NotesVM(
            GetNotes(noteRepository),
            SaveNote(noteRepository),
            UpdateNote(noteRepository),
            RemoveNote(noteRepository)
        )
        notesAdapter = NotesAdapter(notesVM::onUnpinClick, ::showNoteOptions)
        binding.rvNotes.adapter = notesAdapter
    }

    private fun showNoteOptions(note: Note) {
        val modalBottomSheet = NoteOptionsBottomSheet(note)
        modalBottomSheet.show(
            requireActivity().supportFragmentManager,
            NoteOptionsBottomSheet.TAG
        )
    }

    private fun observers() {
        notesVM.notes.observe(viewLifecycleOwner) { notesAdapter.submitList(it) }
        notesVM.attachments.observe(viewLifecycleOwner) { attachment ->
            binding.cgAttachments.removeAllViews()
            attachment.forEach { binding.cgAttachments.addView(createAttachmentChip(it)) }
        }
    }

    private fun listeners() {
        binding.btnTakePhoto.setOnClickListener { multimediaObserver.takePhoto() }
        binding.btnSaveNote.setOnClickListener {
            it.isEnabled = false
            saveFastNote()
            it.isEnabled = true
        }
        binding.btnAddNote.setOnClickListener {
            findNavController().navigate(
                NotesFragmentDirections.actionNotesFragmentToNoteFragment()
            )
        }
    }

    private fun saveFastNote() {
        val textNote = binding.edtNewNote.text.toString()
        if (textNote.isNotEmpty()) {
            //Carpeta privada destino
            requireActivity().hideKeyboard()
            val destinationFolder = requireContext().getExternalFilesDir("notes")
            destinationFolder?.let {
                if (!destinationFolder.exists()) destinationFolder.mkdirs()
                notesVM.attachments.value!!.forEach { uri ->
                    try {
                        val destinationFile = File(destinationFolder, "${uri.fileName()}.jpg")
                        val imgInputStream = requireContext().contentResolver.openInputStream(uri)
                        //Decodifica el stream de entrada en un Bitmap
                        val bitmap = BitmapFactory.decodeStream(imgInputStream)
                        //Crea un stream de salida hacia el archivo de destino
                        val imgOutputStream = FileOutputStream(destinationFile)
                        //Comprime el bitmap en formato JPEG y escribe el stream de salida
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imgOutputStream)
                        imgInputStream?.use { input ->
                            imgOutputStream.use { output -> input.copyTo(output) }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                notesVM.saveFastNote("", textNote)
                binding.btnAddNote.showMessage("Se ha guardado la nota exitosamente")
            }
        }
    }

    private fun clearFastNote() { binding.edtNewNote.setText("") }

    private fun removeNote(note: Note) {
        val privateFolder = File(requireContext().getExternalFilesDir(null), "notes")
        note.imagesPaths.forEach { File(privateFolder, it).delete() }
        //notesVM.removeNote(note)
    }

    private fun createAttachmentChip(uri: Uri) = Chip(binding.cgAttachments.context).apply {
        text = uri.fileName()
        chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_image_24)
        chipIconTint = ContextCompat.getColorStateList(
            requireContext(),
            R.color.md_theme_light_onPrimaryContainer
        )
        isCloseIconVisible = true
        setOnCloseIconClickListener {
            notesVM.removeAttachment(uri)
        }
    }
}