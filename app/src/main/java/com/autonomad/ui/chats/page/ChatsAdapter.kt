package com.autonomad.ui.chats.page

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.autonomad.R
import com.autonomad.data.models.chat.Dialog
import com.autonomad.utils.*
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatsAdapter(private val onClick: (Int, Boolean) -> Unit, override var scrollListener: ScrollListener?) :
    PagedAdapter<Dialog, ChatsAdapter.ChatViewHolder>(ChatDiffUtil()) {

    override fun getItemViewHolder(parent: ViewGroup, viewType: Int) =
        ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false))

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: PagingViewHolder, position: Int) {
        if (holder !is ChatViewHolder) return
        val dialog = getItem(position)
        holder.itemView.apply {
            setOnClickListener { onClick(dialog.id ?: 0, false) }
            setOnLongClickListener {
                onClick(dialog.id ?: 0, true)
                true
            }
            tv_chat_name.text = dialog.chat?.title.orEmpty()
            tv_last_message.text = dialog.topMessage?.text.orEmpty()
            tv_time.text = ""
            val time = DateUtil(dialog.topMessage?.createdAt ?: return@apply)
            tv_time.text = if (time.isToday) {
                val hour = if (time.hour in 0..9) "0${time.hour}" else time.hour.toString()
                val minute = if (time.minute in 0..9) "0${time.minute}" else time.minute.toString()
                "${hour}:${minute}"
            } else "${time.day} ${time.monthStringShort}"
            if (!time.isThisYear) tv_time.text = "${tv_time.text} ${time.year}"
        }
    }

    class ChatViewHolder(view: View) : PagingViewHolder(view)

    class ChatDiffUtil : DiffUtil.ItemCallback<Dialog>() {
        override fun areItemsTheSame(oldItem: Dialog, newItem: Dialog) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Dialog, newItem: Dialog) = oldItem == newItem
    }
}