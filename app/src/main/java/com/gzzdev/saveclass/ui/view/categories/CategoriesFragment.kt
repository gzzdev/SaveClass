package com.gzzdev.saveclass.ui.view.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import com.gzzdev.saveclass.R
import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.data.model.RoomDataSource
import com.gzzdev.saveclass.data.repository.CategoryRepository
import com.gzzdev.saveclass.databinding.FragmentCategoriesBinding
import com.gzzdev.saveclass.domain.GetCategoriesTotalNotes
import com.gzzdev.saveclass.domain.RemoveCategory
import com.gzzdev.saveclass.ui.common.app
import com.gzzdev.saveclass.ui.view.new_category_bottom_sheet.NewCategoryBottomSheet

class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoriesVM: CategoriesVM
    private lateinit var categoriesAdapter: CategoriesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        listeners()
        observers()
    }

    private fun setup() {
        val repo = CategoryRepository(RoomDataSource(requireContext().app.room))
        categoriesVM = CategoriesVM(
            GetCategoriesTotalNotes(repo),
            RemoveCategory(repo)
        )
        categoriesAdapter = CategoriesAdapter(::showMenuDialog)
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun listeners() {
        binding.btnNewCategory.setOnClickListener {
            val modalBottomSheet = NewCategoryBottomSheet()
            modalBottomSheet.show(
                requireActivity().supportFragmentManager,
                NewCategoryBottomSheet.TAG
            )
        }
    }

    private fun observers() {
        categoriesVM.categories.observe(viewLifecycleOwner) { categoriesAdapter.submitList(it) }
    }


    private fun showMenuDialog(viewCategory: View, category: Category) {
        val popupMenu = PopupMenu(requireContext(), viewCategory)
        popupMenu.inflate(R.menu.menu_pop_category)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.category_delete -> {
                    categoriesVM.removeCategory(category)
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
}