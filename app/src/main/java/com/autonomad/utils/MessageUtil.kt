package com.autonomad.utils

import com.autonomad.data.models.Page
import com.autonomad.data.models.chat.Dialog
import com.autonomad.data.models.chat.Message
import com.autonomad.data.models.chat.SocketDialog
import com.autonomad.data.models.chat.SocketMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MessageUtil {
    suspend fun addRealtimeMessage(currentMessages: Page<Message>, socketMessage: SocketMessage) =
        withContext(Dispatchers.Default) {
            val newList = mutableListOf(*currentMessages.list.toTypedArray())
            val message = socketMessage.dialog?.message?.run { toMessage() } ?: return@withContext currentMessages
            var shouldAppend = true
            var i = 0
            if (message.createdAt != null) {
                for ((index, m) in currentMessages.list.withIndex()) {
                    if (m.id == message.id) {
                        newList[index] = message
                        shouldAppend = false
                        break
                    }
                    if (m.createdAt == null || m.createdAt > message.createdAt)
                        continue
                    i = index
                    break
                }
            }
            if (shouldAppend) newList.add(i, message)
            currentMessages.copy(list = newList)
        }

    suspend fun addMessagePage(currentMessages: Page<Message>, newPage: Page<Message>) = withContext(Dispatchers.Default) {
        val linkedMap = currentMessages.list.associateByTo(LinkedHashMap(currentMessages.list.size)) { it.id }
        for (message in newPage.list) {
            linkedMap[message.id] = message
        }
        newPage.copy(list = linkedMap.toList().map { it.second })
    }

    suspend fun updateDialogs(currentDialogs: Page<Dialog>, newDialog: SocketDialog?) = withContext(Dispatchers.Default) {
        val newList = mutableListOf(*currentDialogs.list.toTypedArray())
        for ((i, d) in newList.withIndex()) {
            if (d.id == newDialog?.id) {
                newList[i] = d.copy(topMessage = newDialog?.message?.toMessage())
                break
            }
        }
        currentDialogs.copy(list = newList)
    }

    suspend fun addDialogPage(currentDialogs: Page<Dialog>, newPage: Page<Dialog>) = withContext(Dispatchers.Default) {
        val linkedMap = currentDialogs.list.associateByTo(LinkedHashMap(currentDialogs.list.size)) { it.id }
        for (dialog in newPage.list) {
            linkedMap[dialog.id] = dialog
        }
        newPage.copy(list = linkedMap.toList().map { it.second })
    }
}