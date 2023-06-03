package com.gzzdev.saveclass.data.model.dao

import androidx.room.*
import com.gzzdev.saveclass.data.model.Category
import com.gzzdev.saveclass.data.model.Note
import com.gzzdev.saveclass.data.model.NoteWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note): Long

    @Update
    fun update(note: Note)

    @Transaction
    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<NoteWithCategory>>

    @Query("SELECT COUNT(idNote) FROM note")
    fun getTotalNotes(): Flow<Int>
}