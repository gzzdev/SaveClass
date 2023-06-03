package com.gzzdev.saveclass.data.model

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class  NoteWithCategory(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "category",
        entityColumn = "idCategory"
    )
    val category: Category
): Serializable