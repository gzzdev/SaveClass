package com.gzzdev.saveclass.ui.common

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
        fun bind(note: NoteWithCategory) {
            binding.tvTitle.text = note.note.title.ifEmpty { binding.root.context.getString(R.string.not_title) }
            /*if(note.note.imagesPaths.isNotEmpty()){
                if(note.note.imagesPaths.size > 1) binding.imgMore.visibility = View.VISIBLE
                binding.cvImg.visibility = View.VISIBLE
                val outFile = binding.root.context.getFileStreamPath(note.note.imagesPaths.first() + ".jpeg")
                binding.img.setImageURI(Uri.fromFile(outFile))
            }*/
            if (note.note.text.isNotEmpty()) {
                binding.tvText.visibility = View.VISIBLE
                binding.tvText.text = note.note.text
            }
            binding.tvDate.text = simpleDateFormat.format(note.note.updated)

            binding.btnFavorite.apply {
                icon = if(note.note.isFavorite) binding.root.context.getDrawable(R.drawable.ic_baseline_star_24)
                else binding.root.context.getDrawable(R.drawable.ic_outline_star_outline_24)
                setOnClickListener { onFavoriteClick(note.note) }
            }

            if (note.note.isPin) {
                binding.btnPin.visibility = View.VISIBLE
                binding.btnPin.setOnClickListener {
                    it.visibility = View.GONE
                    onPinClick(note.note)
                }
            } else binding.btnPin.visibility = View.GONE
            binding.root.setOnLongClickListener {
                showMenuDialog(it, note.note)
                true
            }
        }
    }
}
