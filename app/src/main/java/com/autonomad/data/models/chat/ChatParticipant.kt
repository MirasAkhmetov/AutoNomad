package com.autonomad.data.models.chat

import com.autonomad.data.models.login.UserInfo

data class ChatParticipant(
    val id: Int?,
    val user: UserInfo?,
    val type: Int?,
    val status: Int?
)

data class ChatParticipantPost(val user: Int, val chat: String)