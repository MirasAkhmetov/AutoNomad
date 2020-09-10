package com.autonomad.data.models.chat

import com.google.gson.annotations.SerializedName

data class Dialog(
    val id: Int?,
    val chat: Chat?,
    @SerializedName("unread_count")
    val unreadCount: Int?,
    @SerializedName("top_message")
    val topMessage: Message?,
    @SerializedName("auto_request_id")
    val autoRequestId: Int? = null,
    @SerializedName("auto_response_id")
    val autoResponseId: Int? = null
)
