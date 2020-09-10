package com.autonomad.ui.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autonomad.data.models.Page
import com.autonomad.data.models.chat.Dialog
import com.autonomad.data.repo.chat.ChatRepo
import com.autonomad.data.repo.chat.SubscriptionResponse
import com.autonomad.ui.chats.chat.DialogsChannelMsg
import com.autonomad.ui.chats.chat.ListMsg
import com.autonomad.ui.chats.chat.UpdateMsg
import com.autonomad.utils.MessageUtil
import com.autonomad.utils.Status
import com.autonomad.utils.successful
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class ChatsViewModel : ViewModel(), KoinComponent {

    private val repo: ChatRepo by inject()
    private val dialogsChannel = Channel<DialogsChannelMsg>()

    private val mDialogs = MutableLiveData<Status<Page<Dialog>>>()
    val dialogs: LiveData<Status<Page<Dialog>>> by lazy {
        observeDialogsChannel()
        subscribeToMessages()
        mDialogs
    }

    private val mDefault = MutableLiveData<Status<Dialog>>()
    val default: LiveData<Status<Dialog>> by lazy {
        loadDefault()
        mDefault
    }

    private val mDialogsWithMasters = MutableLiveData<Status<Page<Dialog>>>()
    val dialogsWithMasters: LiveData<Status<Page<Dialog>>> by lazy {
        mDialogsWithMasters
    }

    private val dialogsWithMastersChannel = Channel<DialogsChannelMsg>()

    private val mDialogsWithClients = MutableLiveData<Status<Page<Dialog>>>()
    val dialogsWithClients: LiveData<Status<Page<Dialog>>> by lazy {
        mDialogsWithClients
    }

    private val dialogsWithClientsChannel = Channel<DialogsChannelMsg>()

    var isContinue = true
        set(value) {
            if (!value) {
                field = value
                viewModelScope.launch {
                    repo.breakList()
                }
            } else if (!field && value) {
                subscribeToMessages()
                field = value
            }
        }

    private var page = 0
    fun loadDialogs() {
        viewModelScope.launch {
            val status = repo.getDialogs(page)
            if (status?.item != null && status.isSuccess) {
                page++
                dialogsChannel.send(ListMsg(status.item))
            }
            status?.onFail {
                Status.setFailed(mDialogs, it)
            }
        }
    }

    private fun loadDefault() {
        viewModelScope.launch { mDefault.postValue(repo.getDefault()) }
    }

    private fun observeDialogsChannel() {
        viewModelScope.launch {
            for (m in dialogsChannel) {
                if (m is ListMsg) {
                    mDialogs.postValue(MessageUtil.addDialogPage(mDialogs.value?.item ?: Page(), m.page).successful)
                } else if (m is UpdateMsg) {
                    mDialogs.postValue(
                        MessageUtil.updateDialogs(mDialogs.value?.item ?: Page(), m.message.dialog).successful
                    )
                }
            }
        }
        viewModelScope.launch {
            for (m in dialogsWithMastersChannel) {
                if (m is ListMsg) {
                    mDialogsWithMasters.postValue(
                        MessageUtil.addDialogPage(mDialogsWithMasters.value?.item ?: Page(), m.page).successful
                    )
                } else if (m is UpdateMsg) {
                    mDialogsWithMasters.postValue(
                        MessageUtil.updateDialogs(mDialogsWithMasters.value?.item ?: Page(), m.message.dialog).successful
                    )
                }
            }
        }
        viewModelScope.launch {
            for (m in dialogsWithClientsChannel) {
                if (m is ListMsg) {
                    mDialogsWithClients.postValue(
                        MessageUtil.addDialogPage(mDialogsWithClients.value?.item ?: Page(), m.page).successful
                    )
                } else if (m is UpdateMsg) {
                    mDialogsWithClients.postValue(
                        MessageUtil.updateDialogs(mDialogsWithClients.value?.item ?: Page(), m.message.dialog).successful
                    )
                }
            }
        }
    }

    private fun subscribeToMessages() {
        viewModelScope.launch {
            for (m in repo.subscribeToMessages(this)) {
                if (m is SubscriptionResponse.Break) break
                if (m is SubscriptionResponse.Success) {
                    if (m.message.dialog?.message?.autoRequestEvent != null) dialogsWithMastersChannel.send(UpdateMsg(m.message))
                    else dialogsChannel.send(UpdateMsg(m.message))
                } else if (m is SubscriptionResponse.Failure) {
                    Status.setFailed(mDialogs, m.reason)
                }
            }
        }
    }

    private var pageWithMasters = 0
    private fun loadDialogsWithMasters() {
        viewModelScope.launch {
            val status = repo.chatsWithMasters()
            if (status?.isSuccess == true && status.item != null) {
                pageWithMasters++
                dialogsWithMastersChannel.send(ListMsg(status.item))
            }
            status?.onFail {
                Status.setFailed(mDialogsWithMasters, it)
            }
        }
    }

    private var pageWithClients = 0
    private fun loadDialogsWithClients() {
        viewModelScope.launch {
            val status = repo.chatsWithClients()
            if (status?.isSuccess == true && status.item != null) {
                pageWithClients++
                dialogsWithClientsChannel.send(ListMsg(status.item))
            }
            status?.onFail {
                Status.setFailed(mDialogsWithClients, it)
            }
        }
    }
}
