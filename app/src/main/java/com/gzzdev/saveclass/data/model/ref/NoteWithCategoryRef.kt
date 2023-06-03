package com.gzzdev.saveclass.data.model.ref

import androidx.room.Entity

@Entity(primaryKeys = ["category", "idCategory"])
data class NoteWithCategoryRef (
    val category: Int,
    val idCategory: Int
)