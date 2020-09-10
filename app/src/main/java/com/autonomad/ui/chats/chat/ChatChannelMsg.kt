package com.autonomad.ui.chats.chat

import com.autonomad.data.models.Page
import com.autonomad.data.models.chat.Dialog
import com.autonomad.data.models.chat.Message
import com.autonomad.data.models.chat.SocketMessage

sealed class ChatChannelMsg
class PageMsg(val page: Page<Message>) : ChatChannelMsg()
class RealtimeMsg(val message: SocketMessage) : ChatChannelMsg()

sealed class DialogsChannelMsg
class ListMsg(val page: Page<Dialog>) : DialogsChannelMsg() {
    override fun toString() = "ListMsg(page = $page)"
}
class UpdateMsg(val message: SocketMessage) : DialogsChannelMsg() {
    override fun toString() = "UpdateMsg(message = $message)"
}