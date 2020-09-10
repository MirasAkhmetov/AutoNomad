package com.autonomad.data.models.insurance

data class NewCheck(
    val iin: String,
    val tfNumber: String,
    val isFavorite: Boolean = false
)