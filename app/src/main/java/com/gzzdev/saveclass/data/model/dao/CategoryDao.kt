package com.gzzdev.saveclass.data.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gzzdev.saveclass.data.model.CategoriesTotalNotes
import com.gzzdev.saveclass.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category): Long

    @Delete
    fun remove(category: Category)

    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT COUNT(idCategory) FROM category")
    fun getTotalCategories(): Flow<Int>

    @Query("SELECT category.*, COUNT(note.idNote) AS totalNotes FROM category LEFT JOIN note ON note.category=category.idCategory GROUP BY category.idCategory")
    fun getCategoriesTotalNotes(): Flow<List<CategoriesTotalNotes>>
    //SELECT Category.name, Count(Note.id) AS TotalNotes, Count(File.id) FROM Category left join Note on Note.categoryId=Category.id LEFT JOIN File on File.noteId=Note.id group by Category.id;
    //SELECT Category.name, Count(Note.id) AS Total FROM Category left join Note on Note.categoryId=Category.id group by Category.id;
}