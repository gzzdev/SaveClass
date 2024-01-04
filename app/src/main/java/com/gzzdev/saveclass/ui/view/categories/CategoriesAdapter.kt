package com.gzzdev.saveclass.ui.view.categories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.utilities.MathUtils.lerp
import com.gzzdev.saveclass.R
import com.gzzdev.saveclass.data.model.CategoriesTotalNotes
import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.databinding.ItemCategoryBinding

class CategoriesAdapter(
    private val toNotes: (Category) -> Unit,
    private val showMenuDialog: (View, Category) -> Unit
): ListAdapter<CategoriesTotalNotes, CategoriesAdapter.NoteVH>(Companion)  {
    companion object : DiffUtil.ItemCallback<CategoriesTotalNotes>() {
        override fun areItemsTheSame(
            oldItem: CategoriesTotalNotes,
            newItem: CategoriesTotalNotes
        ): Boolean = oldItem.category.idCategory == newItem.category.idCategory

        override fun areContentsTheSame(
            oldItem: CategoriesTotalNotes,
            newItem: CategoriesTotalNotes
        ): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteVH {
        val inflater = LayoutInflater.from(parent.context)
        return NoteVH(ItemCategoryBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: NoteVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NoteVH(private val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("RestrictedApi")
        fun bind(categoryWithTotal: CategoriesTotalNotes) {
            val category = categoryWithTotal.category
            // Setting
            binding.ivBook.drawable.setTint(category.color)
            binding.tvTitle.text = category.name
            binding.tvNNotes.text = binding.root.context.resources
                .getQuantityString(
                    R.plurals.n_notes,
                    categoryWithTotal.totalNotes,
                    categoryWithTotal.totalNotes
                )
            // Listeners
            binding.root.setOnLongClickListener {
                showMenuDialog(it, category)
                true
            }
            binding.root.setOnClickListener { toNotes(category) }
            // Animacion
            binding.root.setOnMaskChangedListener { maskRect ->
                if (maskRect.width() >= 148) binding.ivBook.translationX = maskRect.left
                binding.tvTitle.translationX = maskRect.left
                lerp(1.0, 0.0, maskRect.left/100.0).toFloat().let { opacity ->
                    binding.tvNNotes.alpha = opacity
                    binding.tvTitle.alpha = opacity
                }
            }
        }
    }
}
