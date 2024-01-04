package com.gzzdev.saveclass.ui.common.model

data class Option (
    val id: Int,
    val title: String,
    val action: (Int) -> Unit,
    val icon: Int,
    val iconTonalStyle: Boolean = true
)