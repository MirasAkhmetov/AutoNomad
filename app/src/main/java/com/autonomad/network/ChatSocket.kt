package com.autonomad.network

import com.autonomad.data.models.chat.SocketMessage
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import kotlinx.coroutines.channels.ReceiveChannel

interface ChatSocket {
    @Receive
    fun onConnect(): ReceiveChannel<WebSocket.Event>

    @Receive
    fun listenMessages(): ReceiveChannel<SocketMessage>
}