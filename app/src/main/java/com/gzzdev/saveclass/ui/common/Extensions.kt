package com.gzzdev.saveclass.ui.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gzzdev.saveclass.ui.SaveClassApp

val Context.app: SaveClassApp get() = applicationContext as SaveClassApp

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)

@SuppressLint("RestrictedApi")
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun View.showMessage(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionMessage: CharSequence? = null,
    action: (View) -> Unit = {}
) = Snackbar.make(this, message, duration).apply {
    if (this@showMessage is FloatingActionButton) anchorView = this@showMessage
    actionMessage?.let { setAction(actionMessage, action) }
}.show()

fun Uri.fileName(): String {
    val file = this.lastPathSegment ?: ""
    return file.split(".")[0]
}