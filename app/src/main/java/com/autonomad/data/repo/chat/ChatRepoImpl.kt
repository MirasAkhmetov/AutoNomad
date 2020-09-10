package com.autonomad.data.repo.chat

import com.autonomad.data.models.chat.PostMessage
import com.autonomad.data.models.chat.SocketMessage
import com.autonomad.network.ApiCaps
import com.autonomad.network.ApiChat
import com.autonomad.network.ChatSocket
import com.autonomad.utils.ApiCaller
import com.autonomad.utils.timber
import com.squareup.moshi.Moshi
import com.tinder.scarlet.Message
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.parameter.parametersOf

class ChatRepoImpl(
    private val chatApi: ApiChat,
    private val capsApi: ApiCaps,
    private val dispatcher: CoroutineDispatcher
) : ChatRepo, ApiCaller(dispatcher), KoinComponent {

    private val dialogLimit = 20
    private val messagesLimit = 40

    private val moshi = Moshi.Builder().build().adapter(SocketMessage::class.java)
    private lateinit var chatSocket: ChatSocket
    private val channel = Channel<SubscriptionResponse>()

    private suspend fun getProfile() = apiCallConverted { capsApi.loadProfile() }

    override suspend fun getDialogs(pageNumber: Int) =
        apiCallConverted { chatApi.getDialogs(pageNumber * dialogLimit, dialogLimit) }

    override suspend fun getDefault() = apiCallConverted { chatApi.getDefault() }

    override suspend fun getDialog(dialogId: Int) = apiCallConverted { chatApi.getDialog(dialogId) }

    override suspend fun getMessages(dialogId: Int, lastMessageId: String?) =
        apiCallConverted {
            chatApi.getMessages(
                dialogId,
                lastMessageId,
                messagesLimit
            )
        } // todo server error when last message

    override suspend fun sendMessage(text: String, toChat: String) =
        apiCallConverted { chatApi.sendMessage(PostMessage(text, toChat)) }

    override suspend fun getParticipants(dialogId: Int) = apiCallConverted { chatApi.getParticipants(dialogId) }

    override suspend fun subscribeToMessages(scope: CoroutineScope): ReceiveChannel<SubscriptionResponse> {
        if (!::chatSocket.isInitialized) {
            scope.launch(dispatcher) {
                val profile = getProfile()
                val profileId = profile?.item?.id
                if (profileId == null) {
                    channel.send(SubscriptionResponse.Failure(profile?.message))
                    return@launch
                }
                chatSocket = get { parametersOf(profileId) }
                for (e in chatSocket.onConnect()) {
                    if (e is WebSocket.Event.OnMessageReceived) {
                        withContext(Dispatchers.IO) {
                            val m = moshi.fromJson((e.message as? Message.Text ?: return@withContext).value)
                                ?: return@withContext
                            timber("message: $m")
                            channel.send(SubscriptionResponse.Success(m))
                        }
                    }
                }
            }
        }
        return channel
    }

    override suspend fun chatsWithMasters(pageNumber: Int, autoRequestId: Int?, autoResponseId: Int?) =
        apiCallConverted { chatApi.clientDialogs(dialogLimit * pageNumber, dialogLimit, autoRequestId, autoResponseId) }

    override suspend fun chatsWithClients(pageNumber: Int, autoRequestId: Int?, autoResponseId: Int?) =
        apiCallConverted { chatApi.masterDialogs(dialogLimit * pageNumber, dialogLimit, autoRequestId, autoResponseId) }

    override suspend fun breakList() {
        channel.send(SubscriptionResponse.Break)
    }
}
