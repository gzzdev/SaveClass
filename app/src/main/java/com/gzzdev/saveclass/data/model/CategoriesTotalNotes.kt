package com.gzzdev.saveclass.data.model

import androidx.room.Embedded
import java.io.Serializable

data class  CategoriesTotalNotes(
    @Embedded val category: Category,
    val totalNotes: Int
): Serializable