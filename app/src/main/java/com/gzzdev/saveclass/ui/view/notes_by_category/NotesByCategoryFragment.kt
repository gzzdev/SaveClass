package com.gzzdev.saveclass.ui.view.notes_by_category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.data.model.RoomDataSource
import com.gzzdev.saveclass.data.repository.NoteRepository
import com.gzzdev.saveclass.databinding.FragmentNotesByCategoryBinding
import com.gzzdev.saveclass.domain.notes.GetNotes
import com.gzzdev.saveclass.domain.notes.SaveNote
import com.gzzdev.saveclass.domain.notes.UpdateNote
import com.gzzdev.saveclass.ui.common.NotesAdapter
import com.gzzdev.saveclass.ui.common.app

class NotesByCategoryFragment : Fragment() {

    private var _binding: FragmentNotesByCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesByCategoryVM: NotesByCategoryVM
    private lateinit var notesAdapter: NotesAdapter
    private val args: NotesByCategoryFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesByCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observers()
    }

    private fun setup() {
        binding.topAppBar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.topAppBar.title = args.category.name
        val repo = NoteRepository(RoomDataSource(requireContext().app.room))
        notesByCategoryVM = NotesByCategoryVM(
            GetNotes(repo), SaveNote(repo), UpdateNote(repo), args.category.idCategory
        )
        notesAdapter = NotesAdapter(
            notesByCategoryVM::onFavoriteClick,
            notesByCategoryVM::onPinClick
        )

        binding.rvNotes.adapter = notesAdapter
    }

    private fun observers() {
        notesByCategoryVM.notes.observe(viewLifecycleOwner) { notesAdapter.submitList(it) }
    }


    private fun showMenuDialog(viewNote: View, note: Note) {
        val popupMenu = PopupMenu(requireContext(), viewNote)
        /*popupMenu.inflate(R.menu.menu_pop_note)

        popupMenu.menu.getItem(0).setIcon(
            if(note.isPin) R.drawable.ic_baseline_push_pin_24
            else R.drawable.outline_push_pin_24
        )

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.set_pin -> {
                    notesByCategoryVM.updateNote(note.copy(isPin = !note.isPin))
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
        }*/
    }
}