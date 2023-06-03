package com.gzzdev.saveclass.ui.view.notes

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.gzzdev.saveclass.R
import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.data.model.RoomDataSource
import com.gzzdev.saveclass.data.repository.NoteRepository
import com.gzzdev.saveclass.databinding.FragmentNotesBinding
import com.gzzdev.saveclass.domain.GetNotes
import com.gzzdev.saveclass.domain.SaveNote
import com.gzzdev.saveclass.domain.UpdateNote
import com.gzzdev.saveclass.ui.common.app
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Date

class NotesFragment : Fragment() {
    private val CAMERA_REQUEST_CODE = 0
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesVM: NotesVM
    private lateinit var notesAdapter: NotesAdapter
    private var image: Uri? = null
    private val takePicture = registerForActivityResult(TakePicture()) { isSaved ->
        if (isSaved) {
            binding.btnTakePhoto.isEnabled = false
            binding.ivPhoto.apply {
                visibility = View.VISIBLE
                setImageURI(image)
            }
            binding.btnDeletePhoto.apply {
                setOnClickListener {removePhoto()}
                visibility = View.VISIBLE
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
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
            UpdateNote(noteRepository)
        )
        notesAdapter = NotesAdapter(notesVM::onFavoriteClick, notesVM::onPinClick, ::showMenuDialog)
        binding.rvNotes.adapter = notesAdapter
    }

    private fun showMenuDialog(viewNote: View, note: Note) {
        val popupMenu = PopupMenu(requireContext(), viewNote)
        popupMenu.inflate(R.menu.menu_pop_note)

        popupMenu.menu.getItem(0).setIcon(
            if(note.isPin) R.drawable.ic_baseline_push_pin_24
            else R.drawable.outline_push_pin_24
        )

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.set_pin -> {
                    notesVM.updateNote(note.copy(isPin = !note.isPin))
                    true
                }
                else -> {
                    true
                }
            }
        }

        try {
            val fMenuHelper = PopupMenu::class.java.getDeclaredField("mPopup")
            fMenuHelper.isAccessible = true
            val menuHelper = fMenuHelper.get(popupMenu);
            menuHelper.javaClass.getDeclaredMethod(
                "setForceShowIcon",
                Boolean::class.javaPrimitiveType
            ).invoke(menuHelper, true);
        } catch (e: Exception) {
            Log.d("error", e.toString())
        } finally {
            popupMenu.show()
        }
    }

    private fun observers() {
        notesVM.notes.observe(viewLifecycleOwner) { notesAdapter.submitList(it) }
    }

    private fun listeners() {

        binding.btnTakePhoto.setOnClickListener { checkCameraPermission() }
        binding.btnSaveNote.setOnClickListener {
            val textNote = binding.edtNewNote.text.toString()
            it.isEnabled = false
            if(textNote.isNotEmpty()) {
                var currentDate: Date
                val baos = ByteArrayOutputStream()
                val internalPaths = arrayListOf<String>()
                try {
                    currentDate = Date()
                    val imgInputStream = requireContext().contentResolver.openInputStream(image!!)
                    val bitmap = BitmapFactory.decodeStream(imgInputStream)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val imgOutputStream = requireContext().openFileOutput(
                        currentDate.time.toString() + ".jpeg", Context.MODE_PRIVATE
                    )
                    imgOutputStream.write(baos.toByteArray())
                    imgInputStream?.close()
                    imgOutputStream.close()
                    baos.reset()
                    internalPaths.add(currentDate.time.toString())
                }catch (e: Exception){
                    e.printStackTrace()
                }
                notesVM.saveFastNote("", textNote, internalPaths)
                Snackbar.make(binding.root, "Nota guardada exitosamente", Snackbar.LENGTH_SHORT).show()
                binding.edtNewNote.setText("")
                removePhoto()
                it.isEnabled = true
            }

        }
    }

    private fun removePhoto() {
        binding.btnTakePhoto.isEnabled = true
        image = null
        binding.ivPhoto.apply {
            visibility = View.GONE
            setImageURI(null)
        }
        binding.btnDeletePhoto.visibility = View.GONE
    }

    private fun takePhoto() {
        val photoFile = File.createTempFile(
            Date().time.toString(), ".jpg",
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
        image = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            photoFile
        )
        takePicture.launch(image)
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission()
        } else {
            takePhoto()
        }
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.CAMERA)) {
            Snackbar.make(
                binding.root,
                "Se requiere de permiso para utilizar la cámara.",
                Snackbar.LENGTH_SHORT
            ).show()
        } else {
            //El usuario nunca ha aceptado ni rechazado, así que le pedimos que acepte el permiso.
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto()
                } else {
                    Snackbar.make(
                        binding.root,
                        "Se requiere de permiso para utilizar la cámara.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                return
            }
            else -> {}
        }
    }
}