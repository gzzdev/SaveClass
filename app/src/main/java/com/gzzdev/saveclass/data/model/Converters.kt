package com.gzzdev.saveclass.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.gzzdev.saveclass.ui.common.fromJson
import java.util.*

class Converters {
  @TypeConverter
  fun fromTimestamp(value: Long): Date = Date(value)

  @TypeConverter
  fun dateToTimestamp(date: Date): Long = date.time

  @TypeConverter
  fun fromStringList(value: List<String>): String = Gson().toJson(value)

  @TypeConverter
  fun toStringList(value: String): List<String> = try {
    Gson().fromJson<List<String>>(value)
  } catch (e: Exception) {
    emptyList()
  }
}
