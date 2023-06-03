package com.gzzdev.saveclass.ui.view.new_category_bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
        binding.btnSaveCategory.setOnClickListener {
            newCategoryVM.saveCategory(binding.edtCategory.editText!!.text.toString())
        }
        newCategoryVM.hide.observe(viewLifecycleOwner) { if (it) dismiss() }
        /*val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(
            binding.edtCategory.editText!!,
            0
        )*/
    }

    companion object { const val TAG = "NewCategoryBottomSheet" }

}