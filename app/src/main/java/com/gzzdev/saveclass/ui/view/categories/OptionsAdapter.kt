package com.gzzdev.saveclass.ui.view.categories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.gzzdev.saveclass.ui.common.model.Option
import com.gzzdev.saveclass.databinding.ItemOptionCategoryBinding

class OptionsAdapter(
    private val options: List<Option>
) : BaseAdapter() {
    override fun getCount(): Int = options.size

    override fun getItem(position: Int): Option = options[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val context = parent!!.context
        val binding = if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            ItemOptionCategoryBinding.inflate(inflater, parent, false)
        } else {
            ItemOptionCategoryBinding.bind(convertView)
        }
        val option = getItem(position)
        binding.tvTitle.text = option.title
        val iconButton = if (option.iconTonalStyle) binding.iconTonal else binding.icon
        iconButton.apply {
            visibility = View.VISIBLE
            icon = context.getDrawable(option.icon)
        }
        binding.root.setOnClickListener { option.action(option.id) }
        return binding.root
    }
}