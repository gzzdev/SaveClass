package com.gzzdev.saveclass.ui.common

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import java.text.SimpleDateFormat

object Utils {
    private const val patternDefault = "dd-MM, hh:mm aa"
    val simpleDateFormat = SimpleDateFormat(patternDefault)
    val PASTEL_COLOURS = listOf(
        listOf(
            Color.parseColor("#ff6961"), Color.parseColor("#77dd77"),
            Color.parseColor("#fdfd96"), Color.parseColor("#84b6f4"),
            Color.parseColor("#fdcae1")
        ),
        listOf(
            Color.parseColor("#ffe4e1"), Color.parseColor("#d8f8e1"),
            Color.parseColor("#fcb7af"), Color.parseColor("#b0f2c2"),
            Color.parseColor("#b0c2f2")
        ),
        listOf(
            Color.parseColor("#fabfb7"), Color.parseColor("#fdf9c4"),
            Color.parseColor("#ffda9e"), Color.parseColor("#c5c6c8"),
            Color.parseColor("#b2e2f2")
        ),
        listOf(
            Color.parseColor("#cce5ff"),
            Color.parseColor("#a3ffac"),
            Color.parseColor("#ffca99"),
            Color.parseColor("#eaffc2"),
            Color.parseColor("#ff8097")
        ),
        listOf(
            Color.parseColor("#ff85d5"),
            Color.parseColor("#e79eff"),
            Color.parseColor("#b8e4ff"),
            Color.parseColor("#ff94a2"),
            Color.parseColor("#ffe180")
        )
    )
}