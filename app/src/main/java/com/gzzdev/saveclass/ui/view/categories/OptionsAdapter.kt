package com.gzzdev.saveclass.ui.view.categories

import android.content.Context
import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.gzzdev.saveclass.data.model.Option
import com.gzzdev.saveclass.databinding.ItemOptionCategoryBinding

class OptionsAdapter(
    private val context: Context,
    private val options: List<Option>
): BaseAdapter() {

    override fun getCount(): Int = options.size

    override fun getItem(position: Int): Option = options[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = if (convertView == null) {
            val inflater = LayoutInflater.from(parent!!.context)
            ItemOptionCategoryBinding.inflate(inflater, parent, false)
        } else {
            ItemOptionCategoryBinding.bind(convertView)
        }
        val currentOption = getItem(position)
        binding.tvTitle.text = currentOption.title
        binding.ivIcon.setImageIcon(Icon.createWithResource(parent!!.context, currentOption.icon))
        return binding.root
    }
}