package com.autonomad.ui.chats.chat

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.utils.*
import kotlinx.android.synthetic.main.fragment_chat.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChatFragment : Fragment(R.layout.fragment_chat), ScrollListener {

    private val chatId by lazy { arguments?.getInt(CHAT_ID) ?: 0 }
    private val viewModel: ChatViewModel by viewModel { parametersOf(chatId) }
    private val messageAdapter by lazy { MessageAdapter(chatId, this) }
    private val observers: LifecycleObservers by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(observers.newObserver(javaClass.simpleName))
        iv_back.setOnClickListener { activity?.onBackPressed() }
        rv_messages.apply {
            setHasFixedSize(true)
            layoutManager = object : LinearLayoutManager(requireContext(), VERTICAL, true) {
                override fun isAutoMeasureEnabled() = false
            }
            adapter = messageAdapter
        }
        iv_send.setOnClickListener { sendMessage() }

        viewModel.dialog.observe(viewLifecycleOwner) {
            group_content.isVisible = !it.isLoading
            it.onSuccess {
                tv_chat_name.text = chat?.title.orEmpty()
            }
        }

        viewModel.messages.observe(viewLifecycleOwner) {
            val isEmpty = messageAdapter.items.isEmpty()
            if (it.count <= it.list.size) messageAdapter.finish()
            messageAdapter.items = it.list
            if (isEmpty) rv_messages.scrollToPosition(0)
            if ((rv_messages.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() in 0..2)
                rv_messages.scrollToPosition(0)
        }
        viewModel.status.observe(viewLifecycleOwner, ::tt)
        viewModel.finished.observe(viewLifecycleOwner) {
            if (it) messageAdapter.finish()
        }
    }

    override fun loadMore() {
        viewModel.loadMessages()
    }

    private fun sendMessage() {
        val text = et_message_text.text.toString()
        if (text.isEmpty()) {
            toast(getString(R.string.enter_message_text))
            return
        }
        et_message_text.text.clear()
        viewModel.sendMessage(text)
        rv_messages.scrollToPosition(0)
    }

    companion object {
        const val CHAT_ID = "chatId"
    }
}
