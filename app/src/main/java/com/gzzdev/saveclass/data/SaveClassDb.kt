package com.gzzdev.saveclass.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gzzdev.saveclass.data.model.dao.CategoryDao
import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.data.model.Converters
import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.data.model.dao.NoteDao
import com.gzzdev.saveclass.data.model.ref.NoteWithCategoryRef
import java.util.concurrent.Executors

@Database(entities = [Category::class, Note::class, NoteWithCategoryRef::class], version = 2)
@TypeConverters(Converters::class)
abstract class SaveClassDb: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun noteDao(): NoteDao
}