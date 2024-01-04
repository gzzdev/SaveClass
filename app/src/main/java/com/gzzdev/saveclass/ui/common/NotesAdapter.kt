package com.gzzdev.saveclass.ui.common

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gzzdev.saveclass.R
import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.data.model.NoteWithCategory
import com.gzzdev.saveclass.databinding.ItemNoteBinding
import com.gzzdev.saveclass.ui.common.Utils.simpleDateFormat
import com.gzzdev.saveclass.ui.view.note.ImageSliderAdapter
import java.io.File

class NotesAdapter(
    private val onUnpinClick: (Note) -> Unit,
    private val showOptions: (Note) -> Unit
) : ListAdapter<NoteWithCategory, NotesAdapter.NoteVH>(Companion) {
    companion object : DiffUtil.ItemCallback<NoteWithCategory>() {
        override fun areItemsTheSame(
            oldItem: NoteWithCategory,
            newItem: NoteWithCategory
        ): Boolean = oldItem.note.idNote == newItem.note.idNote

        override fun areContentsTheSame(
            oldItem: NoteWithCategory,
            newItem: NoteWithCategory
        ): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteVH {
        val inflater = LayoutInflater.from(parent.context)
        return NoteVH(ItemNoteBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: NoteVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NoteVH(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(noteWithCategory: NoteWithCategory) {
            val note = noteWithCategory.note
            val category = noteWithCategory.category
            val context = binding.root.context
            binding.apply {
                tvTitle.text = note.title.ifEmpty { context.getString(R.string.no_title) }
                if (note.text.isNotEmpty()) {
                    binding.tvText.visibility = View.VISIBLE
                    binding.tvText.text = note.text
                }
                tvDate.text = simpleDateFormat.format(note.updated)
                if (note.imagesPaths.isNotEmpty()) {
                    val folder = File(context.getExternalFilesDir(null), "notes")
                    rvImages.visibility = View.VISIBLE
                    val imagesAdapter = ImageSliderAdapter()
                    imagesAdapter.submitList(note.imagesPaths.map { name ->
                        getImageUri(context, name, folder)
                    })
                    rvImages.adapter = imagesAdapter
                }
                if (note.isPin) {
                    btnPin.visibility = View.VISIBLE
                    btnPin.setOnClickListener { onUnpinClick(note) }
                }
                root.setOnLongClickListener { showOptions(note); true }
            }
        }

        private fun getImageUri(
            context: Context,
            fileName: String,
            folder: File
        ): Uri {
            val file = File(folder, "$fileName.jpg")
            return file.toUri()
        }
    }
}
