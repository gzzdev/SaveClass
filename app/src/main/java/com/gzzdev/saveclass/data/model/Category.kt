package com.gzzdev.saveclass.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "category")
data class Category(
    var name: String,
    var color: Int,
    @PrimaryKey(autoGenerate = true)
    val idCategory: Int = 0,
): Serializable {
    override fun toString(): String = name
}