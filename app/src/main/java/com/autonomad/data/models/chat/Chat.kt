package com.autonomad.data.models.chat

import com.google.gson.annotations.SerializedName

data class Chat(val id: String?, val title: String?, @SerializedName("created_at") val created: String?)