package com.gzzdev.saveclass.ui.view.note

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gzzdev.saveclass.databinding.ItemImageSliderBinding

class ImageSliderAdapter() : ListAdapter<Uri, ImageSliderAdapter.ImageSliderVH>(Companion) {
    companion object : DiffUtil.ItemCallback<Uri>() {
        override fun areItemsTheSame(
            oldItem: Uri,
            newItem: Uri
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Uri,
            newItem: Uri
        ): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderVH {
        val inflater = LayoutInflater.from(parent.context)
        return ImageSliderVH(ItemImageSliderBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ImageSliderVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ImageSliderVH(
        private val binding: ItemImageSliderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(path: Uri) {
            binding.ivImages.setImageURI(path)
        }
    }
}