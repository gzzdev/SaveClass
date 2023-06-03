package com.gzzdev.saveclass.ui.view.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gzzdev.saveclass.R
import com.gzzdev.saveclass.data.model.CategoriesTotalNotes
import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.databinding.ItemCategoryBinding

class CategoriesAdapter(
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
        fun bind(category: CategoriesTotalNotes) {
            binding.tvTitle.text = category.category.name
            binding.tvNNotes.text = binding.root.context
                .getString(R.string.n_notes, category.totalNotes)
            binding.root.setOnLongClickListener {
                showMenuDialog(it, category.category)
                true
            }
        }
    }
}
