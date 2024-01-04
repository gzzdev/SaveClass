package com.gzzdev.saveclass.ui.view.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gzzdev.saveclass.R
import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.ui.common.model.Option
import com.gzzdev.saveclass.data.model.RoomDataSource
import com.gzzdev.saveclass.data.repository.CategoryRepository
import com.gzzdev.saveclass.databinding.FragmentCategoriesBinding
import com.gzzdev.saveclass.domain.categories.GetCategoriesTotalNotes
import com.gzzdev.saveclass.domain.categories.RemoveCategory
import com.gzzdev.saveclass.ui.common.app
import com.gzzdev.saveclass.ui.common.showMessage
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
        categoriesVM = CategoriesVM(GetCategoriesTotalNotes(repo), RemoveCategory(repo))
        categoriesAdapter = CategoriesAdapter(::toNotes, ::showMenuDialog)
        binding.rvCategories.adapter = categoriesAdapter
        val othersAdapter = OptionsAdapter(listOf(
            Option(0, "Sin categoría", ::showMessage,  R.drawable.baseline_category_24),
            Option(1, "Favoritos", ::showMessage, R.drawable.ic_baseline_star_24),
            Option(2, "Bloqueados", ::showMessage, R.drawable.baseline_lock_24),
            Option(3, "Archivados", ::showMessage, R.drawable.baseline_archive_24),
            Option(4, "Papelera", ::showMessage, R.drawable.baseline_delete_24)
        ))
        val options = resources.getStringArray(R.array.categories_options)
        binding.rvOthers.adapter = othersAdapter
    }

    fun showMessage(id: Int) = binding.root.showMessage(id.toString())
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
        categoriesVM.categories.observe(viewLifecycleOwner) { categories ->
            binding.rvCategories.visibility = if (categories.isEmpty()) View.GONE else View.VISIBLE
            categoriesAdapter.submitList(categories)
        }
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
                else -> { true }
            }
        }

        try {
            val fMenuHelper = PopupMenu::class.java.getDeclaredField("mPopup")
            fMenuHelper.isAccessible = true
            val menuHelper = fMenuHelper.get(popupMenu)
            menuHelper.javaClass.getDeclaredMethod(
                "setForceShowIcon",
                Boolean::class.javaPrimitiveType
            ).invoke(menuHelper, true)
        } catch (e: Exception) {
            Log.d("error", e.toString())
        } finally {
            popupMenu.show()
        }
    }

    private fun toNotes(category: Category) {
        findNavController().navigate(CategoriesFragmentDirections
            .actionCategoriesFragmentToNotesByCategoryFragment(category)
        )
    }
}