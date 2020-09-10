package com.autonomad.ui.chats.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autonomad.data.models.Page
import com.autonomad.data.models.chat.Dialog
import com.autonomad.data.models.chat.Message
import com.autonomad.data.repo.chat.ChatRepo
import com.autonomad.data.repo.chat.SubscriptionResponse
import com.autonomad.utils.MessageUtil
import com.autonomad.utils.Status
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class ChatViewModel(private val repo: ChatRepo, private val chatId: Int) : ViewModel() {

    private val mDialog = MutableLiveData<Status<Dialog>>()
    val dialog: LiveData<Status<Dialog>> by lazy {
        loadChat()
        mDialog
    }

    private val messagesChannel = Channel<ChatChannelMsg>()

    private val mMessages = MutableLiveData<Page<Message>>()
    val messages: LiveData<Page<Message>> by lazy {
        subscribeToMessages()
        observeChannel()
        mMessages
    }

    private val messagesStatus = MutableLiveData<String>()
    val status: LiveData<String> = messagesStatus

    private val mFinished = MutableLiveData<Boolean>()
    val finished: LiveData<Boolean> = mFinished

    private var lastMessageId: String? = null

    private fun loadChat() {
        viewModelScope.launch {
            mDialog.value = repo.getDialog(chatId)
        }
    }

    fun loadMessages() {
        viewModelScope.launch {
            val result = repo.getMessages(chatId, lastMessageId)
            result?.onFail(messagesStatus::postValue)
            result?.item?.let {
                messagesChannel.send(PageMsg(it))
            }
        }
    }

    private fun subscribeToMessages() {
        viewModelScope.launch {
            for (response in repo.subscribeToMessages(this)) {
                if (response is SubscriptionResponse.Success) {
                    messagesChannel.send(RealtimeMsg(response.message))
                } else if (response is SubscriptionResponse.Failure) {
                    messagesStatus.postValue(response.reason)
                }
            }
        }
    }

    private fun observeChannel() {
        viewModelScope.launch {
            for (m in messagesChannel) {
                val currentMessages = mMessages.value ?: Page()
                if (m is PageMsg) {
                    if (m.page.list.isNotEmpty()) {
                        lastMessageId = m.page.list.last().id
                        mMessages.value = MessageUtil.addMessagePage(currentMessages, m.page)
                    } else mFinished.value = true
                } else if (m is RealtimeMsg) {
                    mMessages.value = MessageUtil.addRealtimeMessage(currentMessages, m.message)
                }
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            mDialog.value?.item?.chat?.id?.let { repo.sendMessage(text, it) }
        }
    }
}