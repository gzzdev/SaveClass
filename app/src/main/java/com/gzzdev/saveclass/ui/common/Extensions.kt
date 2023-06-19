package com.gzzdev.saveclass.ui.common

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gzzdev.saveclass.ui.SaveClassApp

val Context.app: SaveClassApp get() = applicationContext as SaveClassApp
inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)

fun Snackbar.showNow(v: View, message: String, duration: Int) {
    Snackbar.make(v, message, duration).show()
}
