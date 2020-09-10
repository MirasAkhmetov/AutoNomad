package com.autonomad.data.repo.chat

import com.autonomad.data.models.Page
import com.autonomad.data.models.chat.ChatParticipant
import com.autonomad.data.models.chat.Dialog
import com.autonomad.data.models.chat.Message
import com.autonomad.utils.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

interface ChatRepo {
    suspend fun getDialogs(pageNumber: Int): Status<Page<Dialog>>?

    suspend fun getDefault(): Status<Dialog>?

    suspend fun getDialog(dialogId: Int): Status<Dialog>?

    suspend fun getMessages(dialogId: Int, lastMessageId: String? = null): Status<Page<Message>>?

    suspend fun getParticipants(dialogId: Int): Status<ChatParticipant>?

    suspend fun sendMessage(text: String, toChat: String): Status<Message>?

    suspend fun subscribeToMessages(scope: CoroutineScope): ReceiveChannel<SubscriptionResponse>

    suspend fun chatsWithMasters(
        pageNumber: Int = 0,
        autoRequestId: Int? = null,
        autoResponseId: Int? = null
    ): Status<Page<Dialog>>?

    suspend fun chatsWithClients(
        pageNumber: Int = 0,
        autoRequestId: Int? = null,
        autoResponseId: Int? = null
    ): Status<Page<Dialog>>?

    suspend fun breakList()
}
