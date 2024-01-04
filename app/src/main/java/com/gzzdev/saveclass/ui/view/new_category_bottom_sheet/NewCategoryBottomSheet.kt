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
import com.gzzdev.saveclass.domain.categories.SaveCategory
import com.gzzdev.saveclass.ui.common.ColorsAdapter
import com.gzzdev.saveclass.ui.common.app

class NewCategoryBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetNewCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var newCategoryVM: NewCategoryVM
    private lateinit var colorsAdapter: ColorsAdapter

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
        setup()
        binding.edtCategory.editText?.requestFocus()
        listeners()
        observers()
    }

    private fun setup() {
        newCategoryVM = NewCategoryVM(
            SaveCategory(
            CategoryRepository(RoomDataSource(requireContext().app.room))
        )
        )
        colorsAdapter = ColorsAdapter(newCategoryVM::checkColor)
        binding.rvColors.adapter = colorsAdapter
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

    private fun observers() {
        newCategoryVM.hide.observe(viewLifecycleOwner) { if (it) dismiss() }
        newCategoryVM.colors.observe(viewLifecycleOwner) {
            colorsAdapter.submitList(it)
        }
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