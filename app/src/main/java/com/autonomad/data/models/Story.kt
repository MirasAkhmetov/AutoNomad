package com.autonomad.data.models

import com.google.gson.annotations.SerializedName

data class Story(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    @SerializedName("story_snaps") val snaps: List<StorySnap>
)

data class StorySnap(
    val id: Int,
    val title: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val likes: Int,
    val dislikes: Int,
    val story: Int,
    val image: String
)