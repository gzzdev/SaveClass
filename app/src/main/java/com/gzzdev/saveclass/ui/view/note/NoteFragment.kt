package com.gzzdev.saveclass.ui.view.note

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.gzzdev.saveclass.R
import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.data.model.RoomDataSource
import com.gzzdev.saveclass.data.repository.CategoryRepository
import com.gzzdev.saveclass.data.repository.NoteRepository
import com.gzzdev.saveclass.databinding.FragmentNoteBinding
import com.gzzdev.saveclass.domain.GetCategories
import com.gzzdev.saveclass.domain.SaveNote
import com.gzzdev.saveclass.ui.common.Utils.simpleDateFormat
import com.gzzdev.saveclass.ui.common.app
import com.gzzdev.saveclass.ui.view.new_category_bottom_sheet.NewCategoryBottomSheet
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

class NoteFragment : Fragment() {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteVM: NoteVM
    private lateinit var noteSpan: Spannable
    private lateinit var imageSliderAdapter: ImageSliderAdapter

    private val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        uri?.let {
            val imagesPaths = imageSliderAdapter.currentList.toMutableList()
            imagesPaths.add(it)
            if(imagesPaths.isEmpty()) {
                binding.rvImages.visibility = View.GONE
            } else {
                binding.rvImages.visibility = View.VISIBLE
                imageSliderAdapter.submitList(imagesPaths)
            }
        }
    }

    private val formatTextListener = object :  MaterialButtonToggleGroup.OnButtonCheckedListener {
        override fun onButtonChecked(group: MaterialButtonToggleGroup?, checkedId: Int, isChecked: Boolean) {
            when (checkedId) {
                binding.btnBold.id -> changeTextStyle(StyleSpan(Typeface.BOLD) as Any, isChecked, StyleSpan(Typeface.BOLD) as Any)
                binding.btnItalic.id -> changeTextStyle(StyleSpan(Typeface.ITALIC) as Any, isChecked, StyleSpan(Typeface.ITALIC) as Any)
                binding.btnUnderlined.id -> changeTextStyle(UnderlineSpan() as Any, isChecked, UnderlineSpan() as Any)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataSource = RoomDataSource(requireContext().app.room)
        noteVM = NoteVM(
            GetCategories(CategoryRepository(dataSource)),
            SaveNote(NoteRepository(dataSource))
        )
        noteSpan = binding.edtNote.text as Spannable
        setup()
        observers()
        listeners()
    }

    private fun setup() {
        binding.topAppBar.setNavigationOnClickListener {
            val title = binding.edtTitle.text.toString()
            val note = binding.edtNote.text.toString()
            if (imageSliderAdapter.currentList.isEmpty() && title.isEmpty() && note.isEmpty()) {
                findNavController().popBackStack()
            } else {
                var imgInputStream: InputStream
                var imgOutputStream: FileOutputStream
                val baos = ByteArrayOutputStream()
                var bitmap: Bitmap
                var currentDate: Date
                val internalPaths = arrayListOf<String>()
                imageSliderAdapter.currentList.forEach {
                    try {
                        currentDate = Date()
                        imgInputStream = requireContext().contentResolver.openInputStream(it)!!
                        bitmap = BitmapFactory.decodeStream(imgInputStream)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        imgOutputStream = requireContext().openFileOutput(
                            currentDate.time.toString() + ".jpeg", Context.MODE_PRIVATE
                        )
                        imgOutputStream.write(baos.toByteArray())
                        imgInputStream.close()
                        imgOutputStream.close()
                        baos.reset()
                        internalPaths.add(currentDate.time.toString())
                    }catch (e: Exception){ e.printStackTrace() }
                }
                noteVM.saveNote(title, note, internalPaths)
            }
        }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.textFormat -> {
                    binding.cdFormatTextOptions.visibility = if (binding.cdFormatTextOptions.visibility == View.GONE)
                        View.VISIBLE
                        else View.GONE
                    true
                }
                R.id.attachFile -> {
                    pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    true
                }
                else -> false
            }
        }
        imageSliderAdapter = ImageSliderAdapter()
        binding.rvImages.adapter = imageSliderAdapter
    }

    private fun observers() {
        noteVM.categories.observe(viewLifecycleOwner) { categories ->
            binding.spCategories.adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_list_item_1, categories
            )
        }
        noteVM.hide.observe(viewLifecycleOwner) {
            if(it) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.msg_note_saved),
                    Snackbar.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            }
        }
    }
    private fun listeners() {
        var date: String
        var words = 0
        binding.edtNote.doOnTextChanged { _, _, _, _ ->
            date = simpleDateFormat.format(Date())
            words = binding.edtNote.text!!.split("\\s+".toRegex()).size
            binding.tvHeadNote.text = getString(
                R.string.note_head, date,
                words
            )
        }
        binding.edtNote.setOnClickListener {
            binding.formatTextOptions.removeOnButtonCheckedListener(formatTextListener)
            binding.formatTextOptions.clearChecked()
            val spans = noteSpan.getSpans(binding.edtNote.selectionStart,
                binding.edtNote.selectionEnd, Any::class.java)
            spans.forEach { span ->
                if(span is StyleSpan ) {
                    when (span.style) {
                        Typeface.BOLD -> binding.btnBold.isChecked = true
                        Typeface.ITALIC -> binding.btnItalic.isChecked = true
                    }
                }
            }
            binding.formatTextOptions.addOnButtonCheckedListener(formatTextListener)
        }
        binding.formatTextOptions.addOnButtonCheckedListener(formatTextListener)
        binding.btnAddTag.setOnClickListener { binding.chTagGroup.addChip(binding.root.context, "apoko") }
        binding.btnCategory.setOnClickListener {
            val modalBottomSheet = NewCategoryBottomSheet()
            modalBottomSheet.show(requireActivity().supportFragmentManager, NewCategoryBottomSheet.TAG)
        }

        binding.spCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                noteVM.setCategorySelected(binding.spCategories.selectedItem as Category)
            }
        }
    }
    private fun changeTextStyle(textStyle: Any, isChecked: Boolean, textStyle2: Any) {
        val start = binding.edtNote.selectionStart
        val end = binding.edtNote.selectionEnd
        val spanEI = if (start == end) Spanned.SPAN_INCLUSIVE_EXCLUSIVE else Spanned.SPAN_INCLUSIVE_INCLUSIVE
        var currentSpanStart: Int
        var currentSpanEnd: Int
        val spans = noteSpan.getSpans(start, end, textStyle::class.java)
        if (isChecked) {
            if (spans.isEmpty()) noteSpan.setSpan(textStyle, start, end, spanEI)
            spans.forEach { span ->
                currentSpanStart = noteSpan.getSpanStart(span)
                currentSpanEnd = noteSpan.getSpanEnd(span)
                noteSpan.removeSpan(span)
                if (currentSpanStart >= start && currentSpanEnd <= end) {
                    Log.d("apoko", "check 1")
                    noteSpan.setSpan(textStyle, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                } else if (currentSpanStart <= start && currentSpanEnd <= end) {
                    Log.d("apoko", "check 2")
                    noteSpan.setSpan(textStyle, currentSpanStart, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                } else if (currentSpanStart >= start && currentSpanEnd >= end) {
                    Log.d("apoko", "check 3")
                    noteSpan.setSpan(textStyle, start, currentSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                }
            }
        } else {
            spans.forEach { span ->
                currentSpanStart = noteSpan.getSpanStart(span)
                currentSpanEnd = noteSpan.getSpanEnd(span)
                noteSpan.removeSpan(span)
                if(currentSpanStart <= start && currentSpanEnd >= end) {
                    Log.d("apoko", "uncheck 1")
                    noteSpan.setSpan(textStyle, currentSpanStart, start, spanEI)
                    noteSpan.setSpan(textStyle2, end, currentSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                } else if (currentSpanStart <= start && currentSpanEnd <= end) {
                    Log.d("apoko", "uncheck 2")
                    noteSpan.setSpan(textStyle, currentSpanStart, start, spanEI)
                } else if (currentSpanStart >= start && currentSpanEnd >= end) {
                    Log.d("apoko", "uncheck 3")
                    noteSpan.setSpan(textStyle, end, currentSpanEnd, spanEI)
                } else if(currentSpanStart >= start && currentSpanEnd <= end) {
                    Log.d("apoko", "uncheck 4")
                }
            }
        }
    }
    private fun ChipGroup.addChip(context: Context, label: String){
        Chip(context).apply {
            id = View.generateViewId()
            text = label
            isCloseIconVisible = true
            addView(this)
        }
    }
}