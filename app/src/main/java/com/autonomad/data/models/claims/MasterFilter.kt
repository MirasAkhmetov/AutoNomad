package com.autonomad.data.models.claims

data class MasterFilter(val id: Int, val name: String, val type: Type, val sortOrder: Boolean) {
    enum class Type {
        RATING,
        POPULARITY
    }
}