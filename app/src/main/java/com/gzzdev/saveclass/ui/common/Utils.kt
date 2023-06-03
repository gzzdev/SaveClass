package com.gzzdev.saveclass.ui.common

import java.text.SimpleDateFormat

object Utils {
    private const val patternDefault = "dd-MM, hh:mm aa"
    val simpleDateFormat = SimpleDateFormat(patternDefault)
    val PASTEL_COLOURS = arrayOf(
        arrayOf("#ff6961", "#77dd77", "#fdfd96", "#84b6f4", "#fdcae1"),
        arrayOf("#ffe4e1", "#d8f8e1", "#fcb7af", "#b0f2c2", "#b0c2f2"),
        arrayOf("#fabfb7", "#fdf9c4", "#ffda9e", "#c5c6c8", "#b2e2f2"),
        arrayOf("#cce5ff", "#a3ffac", "#ffca99", "#eaffc2", "#ff8097"),
        arrayOf("#ff85d5", "#e79eff", "#b8e4ff", "#ff94a2", "#ffe180")
    )
}