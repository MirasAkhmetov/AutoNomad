package com.autonomad.data.models.claims

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.autonomad.R

class ReviewFilter(val id: Int, val name: String) {
    var isSelected: Boolean = false
    var type: Types =
        Types.ALL

    enum class Types {
        ALL,
        POSITIVE,
        NEGATIVE,
        NEUTRAL
    }
}

@DrawableRes
fun ReviewFilter.getBackground(): Int {
    return if (isSelected) {
        R.drawable.background_grey_3_12dp_rectangle
    } else {
        R.drawable.background_grey_8_16dp_rectangle
    }
}

@ColorRes
fun ReviewFilter.getTextColor(): Int {
    return if (isSelected) {
        R.color.white
    } else {
        R.color.grey_2
    }
}