package com.hari.tmdb.ext

import java.util.*

fun String.sortByEnumNameToDisplayValue(): String {
    if (this.isEmpty()) return ""

    val hyphenRemovedValue = this.replace("_", " ")
    val split = hyphenRemovedValue.split(" ")
    var spitedLowerCaseString = ""
    split.forEach {
        spitedLowerCaseString += it.toLowerCase(Locale.getDefault()).capitalize()+" "
    }

    return spitedLowerCaseString.trim()
}
