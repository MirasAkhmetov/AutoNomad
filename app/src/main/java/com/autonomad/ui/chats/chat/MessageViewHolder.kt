package com.autonomad.ui.chats.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.autonomad.R
import com.autonomad.data.models.chat.Message
import com.autonomad.utils.DateUtil
import com.autonomad.utils.PagingViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_message_received.view.*
import kotlinx.android.synthetic.main.item_message_received_content.view.*
import kotlinx.android.synthetic.main.item_message_sent.view.*

sealed class MessageViewHolder(@LayoutRes layoutId: Int, parent: ViewGroup) :
    PagingViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

    protected abstract val pointer: Group
    protected abstract val text: TextView
    protected abstract val avatar: ImageView?
    protected abstract val time: TextView
    protected abstract val date: TextView

    class Received(parent: ViewGroup) : MessageViewHolder(R.layout.item_message_received, parent) {
        override val pointer: Group = itemView.group_pointer_received
        override val text: TextView = itemView.tv_message_received
        override val avatar: ImageView = itemView.iv_avatar_received
        override val time: TextView = itemView.tv_time_received
        override val date: TextView = itemView.tv_date_received
    }

    class Sent(parent: ViewGroup) : MessageViewHolder(R.layout.item_message_sent, parent) {
        override val pointer: Group = itemView.group_pointer_sent
        override val text: TextView = itemView.tv_message_sent
        override val avatar: ImageView? = null
        override val time: TextView = itemView.tv_time_sent
        override val date: TextView = itemView.tv_date_sent
    }

    class Content(parent: ViewGroup) : MessageViewHolder(R.layout.item_message_received_content, parent) {
        override val pointer: Group = itemView.group_pointer_content
        override val text: TextView = itemView.tv_message_content
        override val avatar: ImageView = itemView.iv_avatar_content
        override val time: TextView = itemView.tv_time_content
        override val date: TextView = itemView.tv_date_content

        private val header: TextView = itemView.tv_header
        private val image: ImageView = itemView.iv_poster
        private val button: Button = itemView.btn_action

        override fun bind(message: Message, withPointer: Boolean, showDate: Boolean) {
            super.bind(message, withPointer, showDate)
            header.isVisible = false
            image.isVisible = false
            button.isVisible = false
        }
    }

    @SuppressLint("SetTextI18n")
    open fun bind(message: Message, withPointer: Boolean, showDate: Boolean) {
        pointer.isVisible = withPointer
        if (withPointer) {
            time.text = message.createdAt.orEmpty().substringAfter("T").substring(0, 5)
            if (avatar != null && message.sender?.user?.avatar?.isNotEmpty() == true) Picasso.get()
                .load(message.sender.user.avatar).error(R.drawable.ic_profile_male)
                .placeholder(R.drawable.ic_profile_male).into(avatar)
        }
        text.text = message.text.orEmpty()
        date.isVisible = showDate
        if (showDate) {
            val d = DateUtil(message.createdAt.orEmpty())
            if (d.isToday) date.setText(R.string.today)
            date.text = "${d.day} ${d.monthString}"
            if (!d.isThisYear) date.text = "${date.text} ${d.year}"
        }
    }
}