package com.gzzdev.saveclass.ui.view.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gzzdev.saveclass.R
import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.databinding.BottomSheetNoteOptionsBinding
import com.gzzdev.saveclass.ui.common.showMessage

class NoteOptionsBottomSheet(private val note: Note) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetNoteOptionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetNoteOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
        setup()
    }

    private fun setup() {
        binding.tvNoteTitle.text = note.title.ifEmpty { getString(R.string.no_title) }
    }
    private fun listeners() {
        //binding.btnClose.setOnClickListener { dismissNow() }
    }
    private fun showMessage(id: Int) = binding.root.showMessage(id.toString())

    companion object {
        const val TAG = "NoteOptionsBottomSheet"
    }
}