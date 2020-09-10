package com.autonomad.data.models.chat

import com.google.gson.annotations.SerializedName

data class Message(
    val id: String?,
    @SerializedName("from_participant")
    val sender: ChatParticipant?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("to_chat")
    val toChat: String?,
    val text: String?,
    @SerializedName("is_hidden")
    val isHidden: Boolean?,
    @SerializedName("is_silent")
    val isSilent: Boolean?,
    @SerializedName("media_payload")
    val media: List<MediaPayload>? = null
)

data class MediaPayload(val photo: String?, val document: String?, val contact: Int?, val address: Int?, val type: Int?)

data class PostMessage(val text: String, @SerializedName("to_chat") val toChat: String)