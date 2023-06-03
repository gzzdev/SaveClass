package com.gzzdev.saveclass.ui

import android.app.Application
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gzzdev.saveclass.data.SaveClassDb
import java.util.concurrent.Executors

class SaveClassApp: Application() {
    lateinit var room: SaveClassDb
    private set
    override fun onCreate() {
        super.onCreate()
        room = Room.databaseBuilder(this, SaveClassDb::class.java, "SaveClassApp")
            .addCallback(object : RoomDatabase.Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val values = ContentValues().apply {
                        put("name", "Sín categoría")
                        put("color", "0")
                    }
                    Executors.newSingleThreadScheduledExecutor().execute {
                        db.insert("Category", SQLiteDatabase.CONFLICT_IGNORE, values)
                    }
                }
            })
            .fallbackToDestructiveMigration().build()
    }
}