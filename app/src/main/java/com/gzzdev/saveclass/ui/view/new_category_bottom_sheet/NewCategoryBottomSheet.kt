package com.gzzdev.saveclass.ui.view.new_category_bottom_sheet

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.gzzdev.saveclass.data.model.RoomDataSource
import com.gzzdev.saveclass.data.repository.CategoryRepository
import com.gzzdev.saveclass.databinding.BottomSheetNewCategoryBinding
import com.gzzdev.saveclass.domain.SaveCategory
import com.gzzdev.saveclass.ui.common.app

class NewCategoryBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetNewCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var newCategoryVM: NewCategoryVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetNewCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newCategoryVM = NewCategoryVM(SaveCategory(
            CategoryRepository(RoomDataSource(requireContext().app.room))
        ))
        binding.edtCategory.editText?.requestFocus()
        listeners()
        newCategoryVM.hide.observe(viewLifecycleOwner) { if (it) dismiss() }
    }

    private fun listeners() {
        binding.btnSaveCategory.setOnClickListener { saveCategory() }
        binding.edtCategory.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.btnSaveCategory.performClick()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun saveCategory() {
        val categoryName = binding.edtCategory.editText!!.text.toString().trim()
        if (categoryName.isNotEmpty()) newCategoryVM.saveCategory(categoryName)
        else Snackbar
            .make(binding.root, "Sin nombre para la categoría", Snackbar.LENGTH_SHORT)
            .show()
    }
    companion object { const val TAG = "NewCategoryBottomSheet" }

}