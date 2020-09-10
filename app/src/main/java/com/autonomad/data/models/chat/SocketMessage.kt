package com.autonomad.data.models.chat

import com.autonomad.data.models.login.UserInfo
import com.squareup.moshi.Json

data class SocketMessage(
    val type: String?,
    @field:Json(name = "dialogue")
    val dialog: SocketDialog?
)

data class SocketDialog(
    val id: Int?,
    val pinned: Boolean?,
    @field:Json(name = "top_message")
    val message: TopMessage?
)

data class TopMessage(
    val id: String?,
    @field:Json(name = "from_participant")
    val sender: FromParticipant?,
    val text: String?,
    @field:Json(name = "created_at")
    val createdAt: String?,
    @field:Json(name = "updated_at")
    val updatedAt: String?,
    @field:Json(name = "message_type")
    val messageType: Int?,
    @field:Json(name = "participation_event")
    val participationEvent: String?,
    @field:Json(name = "auto_request_event")
    val autoRequestEvent: String?,
    val media: List<MediaPayload>?
) {
    fun toMessage() = Message(
        id,
        ChatParticipant(
            sender?.id,
            UserInfo(
                first_name = sender?.firstName,
                last_name = sender?.lastName,
                avatar = sender?.avatar,
                email = null,
                patronymic = null,
                phone = null
            ),
            sender?.type,
            sender?.status
        ),
        createdAt,
        updatedAt,
        null,
        text,
        isHidden = false,
        isSilent = false,
        media = media
    )
}

data class FromParticipant(
    val id: Int,
    @field:Json(name = "first_name")
    val firstName: String?,
    @field:Json(name = "last_name")
    val lastName: String?,
    val avatar: String?,
    val type: Int?,
    val status: Int?
)