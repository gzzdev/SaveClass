package com.gzzdev.saveclass.ui.common

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gzzdev.saveclass.data.model.ColorCategory
import com.gzzdev.saveclass.databinding.ItemColorBinding

class ColorsAdapter(
    private val onCheck: (ColorCategory) -> Unit
): ListAdapter<ColorCategory, ColorsAdapter.ColorVH>(Companion)  {
    companion object : DiffUtil.ItemCallback<ColorCategory>() {
        override fun areItemsTheSame(
            oldItem: ColorCategory,
            newItem: ColorCategory
        ): Boolean = oldItem.color == newItem.color

        override fun areContentsTheSame(
            oldItem: ColorCategory,
            newItem: ColorCategory
        ): Boolean {
            Log.d("apoko_valid", "$oldItem - $newItem -> ${oldItem == newItem}")
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorVH {
        val inflater = LayoutInflater.from(parent.context)
        return ColorVH(ItemColorBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ColorVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ColorVH(
        private val binding: ItemColorBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(color: ColorCategory) {
            binding.root.setOnClickListener { onCheck(color) }
            binding.ivChecked.visibility = if (color.checked) View.VISIBLE else View.GONE
            binding.iv.drawable.setTint(color.color)
        }
    }
}