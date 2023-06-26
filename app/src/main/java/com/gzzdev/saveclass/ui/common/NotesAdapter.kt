package com.gzzdev.saveclass.ui.common

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val onFavoriteClick: (Note) -> Unit,
    private val onPinClick: (Note) -> Unit,
    private val showMenuDialog: (View, Note) -> Unit
): ListAdapter<NoteWithCategory, NotesAdapter.NoteVH>(Companion)  {
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

    inner class NoteVH(private val binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(noteWithCategory: NoteWithCategory) {
            val note = noteWithCategory.note
            val category = noteWithCategory.category
            binding.tvTitle.text = note.title.ifEmpty { binding.root.context.getString(R.string.not_title) }
            if(note.imagesPaths.isNotEmpty()){
                binding.rvImages.visibility = View.VISIBLE
                val imagesAdapter = ImageSliderAdapter()
                imagesAdapter.submitList(note.imagesPaths.map { imgName ->
                    getFileUriFromPrivateFolder(binding.root.context, imgName)
                })
                binding.rvImages.adapter =imagesAdapter
            } else {
                binding.rvImages.visibility = View.GONE
            }
            if (note.text.isNotEmpty()) {
                binding.tvText.visibility = View.VISIBLE
                binding.tvText.text = note.text
            }
            binding.tvDate.text = simpleDateFormat.format(note.updated)
            binding.btnFavorite.apply {
                icon = if(note.isFavorite)
                    binding.root.context.getDrawable(R.drawable.ic_baseline_star_24)
                else binding.root.context.getDrawable(R.drawable.ic_outline_star_outline_24)
                setOnClickListener { onFavoriteClick(note) }
            }

            if (note.isPin) {
                binding.btnPin.visibility = View.VISIBLE
                binding.btnPin.setOnClickListener {
                    it.visibility = View.GONE
                    onPinClick(note)
                }
            } else binding.btnPin.visibility = View.GONE
            binding.root.setOnLongClickListener {
                showMenuDialog(it, note)
                true
            }
        }
        private fun getFileUriFromPrivateFolder(context: Context, fileName: String, folderName: String = "notes"): Uri? {
            val privateFolder = File(context.getExternalFilesDir(null), folderName)
            val file = File(privateFolder, fileName)
            return if (file.exists()) {
                Uri.fromFile(file)
            } else {
                null
            }
        }
    }
}
