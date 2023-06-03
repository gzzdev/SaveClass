package com.gzzdev.saveclass.ui.view.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gzzdev.saveclass.data.model.RoomDataSource
import com.gzzdev.saveclass.data.repository.CategoryRepository
import com.gzzdev.saveclass.data.repository.NoteRepository
import com.gzzdev.saveclass.databinding.FragmentSettingsBinding
import com.gzzdev.saveclass.domain.GetTotalCategories
import com.gzzdev.saveclass.domain.GetTotalNotes
import com.gzzdev.saveclass.ui.common.app

class SettingsFragment: Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingsVM: SettingsVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        listeners()
        observers()

    }
    private fun setup() {
        childFragmentManager.beginTransaction()
            .replace(binding.flPreferences.id, PreferencesFragment()).commit()
        val roomDataSource = RoomDataSource(requireContext().app.room)
        settingsVM = SettingsVM(
            GetTotalNotes(NoteRepository(roomDataSource)),
            GetTotalCategories(CategoryRepository(roomDataSource))
        )
    }
    private fun listeners() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun observers() {
        settingsVM.totalNotes.observe(viewLifecycleOwner) {
            binding.tvNotes.text = it.toString()
        }
        settingsVM.totalCategories.observe(viewLifecycleOwner) {
            binding.tvCategories.text = it.toString()
        }
    }
}