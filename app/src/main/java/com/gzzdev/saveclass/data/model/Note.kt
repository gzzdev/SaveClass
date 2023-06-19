package com.gzzdev.saveclass.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity(tableName = "note", foreignKeys = [ForeignKey(
    entity = Category::class, parentColumns = ["idCategory"], childColumns = ["category"], onDelete = CASCADE
)])
data class Note(
    var category: Int,
    var title: String,
    var text: String,
    var created: Date,
    var updated: Date,
    var imagesPaths: ArrayList<String> = arrayListOf(),
    var isFavorite: Boolean = false,
    var isPin: Boolean = false,
    var isLock: Boolean = false,
    var isArchive: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val idNote: Int = 0
): Serializable