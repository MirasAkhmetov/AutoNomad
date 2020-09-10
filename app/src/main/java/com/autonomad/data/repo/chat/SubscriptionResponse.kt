package com.autonomad.data.repo.chat

import com.autonomad.data.models.chat.SocketMessage

sealed class SubscriptionResponse {
    class Success(val message: SocketMessage) : SubscriptionResponse() {
        override fun toString() = "SubscriptionSuccess(message = $message)"
    }
    class Failure(val reason: String?) : SubscriptionResponse() {
        override fun toString() = "SubscriptionFailure(reason = $reason)"
    }

    object Break : SubscriptionResponse()
}
