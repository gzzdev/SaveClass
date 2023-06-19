package com.gzzdev.saveclass.ui.view.categories

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.MaskableFrameLayout
import com.google.android.material.color.utilities.MathUtils.lerp
import com.gzzdev.saveclass.R
import com.gzzdev.saveclass.data.model.CategoriesTotalNotes
import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.databinding.ItemCategoryBinding
import kotlin.math.floor

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
            binding.tvTitle.text = category.name
            binding.tvNNotes.text = binding.root.context
                .getString(R.string.n_notes, categoryWithTotal.totalNotes)
            binding.root.setOnLongClickListener {
                showMenuDialog(it, category)
                true
            }
            binding.root.setOnClickListener { toNotes(category) }
            binding.root.setOnMaskChangedListener { maskRect ->
                binding.tvTitle.translationX = maskRect.left
                var start: Double
                var end: Double
                if (maskRect.left > 0.8) {
                    binding.tvTitle.visibility = View.INVISIBLE
                    binding.tvNNotes.visibility = View.INVISIBLE
                    binding.ivCategory.visibility = View.INVISIBLE
                    start = 0.toDouble()
                    end = 1.toDouble()
                } else {
                    start = 1.toDouble()
                    end = 0.toDouble()
                    binding.ivCategory.visibility = View.VISIBLE
                    binding.tvTitle.visibility = View.VISIBLE
                    binding.tvNNotes.visibility = View.VISIBLE
                }
                //binding.tvTitle.alpha = lerp(start, end, maskRect.left.toDouble()).toFloat()
                binding.tvTitle.alpha = lerp(start, end, floor(maskRect.left.toDouble())).toFloat()
            }
        }
    }
}
