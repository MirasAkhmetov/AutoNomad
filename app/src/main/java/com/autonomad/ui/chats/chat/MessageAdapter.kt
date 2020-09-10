package com.autonomad.ui.chats.chat

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.autonomad.data.models.chat.Message
import com.autonomad.utils.*

class MessageAdapter(private val userId: Int, override var scrollListener: ScrollListener?) :
    PagedAdapter<Message, MessageViewHolder>(MessageDiffUtil(), 10) {

    override fun getItemViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == TYPE_SENT) MessageViewHolder.Sent(parent) else MessageViewHolder.Received(parent)

    override fun onBindViewHolder(holder: PagingViewHolder, position: Int) {
        if (holder is MessageViewHolder) {
            val message = getItem(position)
            var showDate = position == items.lastIndex
            var withPointer = position == 0 || message.sender != getItem(position - 1).sender
            val current = DateUtil(message.createdAt.orEmpty())
            if (position != 0) {
                val previous = DateUtil(getItem(position - 1).createdAt.orEmpty())
                if (!withPointer && previous.millis - current.millis > 1000 * 60 * 5)
                    withPointer = true
            }
            if (!showDate) {
                val next = DateUtil(getItem(position + 1).createdAt.orEmpty())
                if (!showDate && (next.day != current.day || next.month != current.month || next.year != current.year))
                    showDate = true
            }
            holder.bind(message, withPointer, showDate)
        }
    }

    override fun getItemType(position: Int) = if (getItem(position).sender?.id != userId) TYPE_RECEIVED else TYPE_SENT

    companion object {
        private const val TYPE_RECEIVED = 0
        private const val TYPE_SENT = 1
    }

    private class MessageDiffUtil : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem
    }
}
