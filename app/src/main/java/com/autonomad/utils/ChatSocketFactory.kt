package com.autonomad.utils

import android.app.Application
import com.autonomad.network.ChatSocket
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory

class ChatSocketFactory(
    private val application: Application,
    private val lifecycle: LifecycleObservers,
    userId: Int
) {

    private val url = "${Constants.BASE_URL_CHAT_SOCKET}$userId/"

    val socketService by lazy {
        scarlet.create<ChatSocket>()
    }

    private val scarlet: Scarlet by lazy {
        Scarlet.Builder()
            .lifecycle(AndroidLifecycle.ofApplicationForeground(application).combineWith(lifecycle))
            .webSocketFactory(HttpClient.tokenClient.newWebSocketFactory(url))
            .backoffStrategy(ExponentialWithJitterBackoffStrategy(5000, 15000))
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .build()
    }
}
