package com.autonomad.ui.chats.page

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.autonomad.R
import com.autonomad.ui.chats.ChatsViewModel
import com.autonomad.ui.chats.chat.ChatFragment
import com.autonomad.utils.ScrollListener
import com.autonomad.utils.timber
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_chats_page.*

class ChatsPageFragment : Fragment(R.layout.fragment_chats_page), ScrollListener {

    private val adapter = ChatsAdapter(::onChatClick, this)
    private val position by lazy { arguments?.getInt(POSITION) ?: 0 }
    private val viewModel by navGraphViewModels<ChatsViewModel>(R.id.chats)

    private var error: String? = null
        set(value) {
            if (value == field || field == null) return
            timber(value)
            field = value
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_chats.apply {
            setHasFixedSize(true)
            adapter = this@ChatsPageFragment.adapter
        }
        if (position == 0) {
            viewModel.default.observe(viewLifecycleOwner) {
                it.onSuccess {
                    if (!rv_chats.isVisible) rv_chats.isVisible = true
                    adapter.items = listOf(this)
                    adapter.finish()
                }
                it.onFail { message ->
                    timber(message)
                    error = message
                    if (adapter.items.isEmpty()) rv_chats.isVisible = false
                    else adapter.setLoaded()
                }
            }
        } else {
            viewModel.dialogs.observe(viewLifecycleOwner) {
                it.onSuccess {
                    if (!rv_chats.isVisible) rv_chats.isVisible = true
                    adapter.items = list
                    if (next == null) adapter.finish()
                }
                it.onFail { message ->
                    timber(message)
                    error = message
                    if (adapter.items.isEmpty()) rv_chats.isVisible = false
                    else adapter.setLoaded()
                }
            }
        }
    }

    override fun onResume() {
        viewModel.isContinue = true
        super.onResume()
    }

    override fun loadMore() {
        if (position != 0) viewModel.loadDialogs()
    }

    private fun onChatClick(id: Int, isLongClick: Boolean) {
        if (id == 0) {
            toast("Возникли проблемы с получением данных")
            return
        }
        if (!isLongClick) {
            viewModel.isContinue = false
            findNavController().navigate(
                R.id.action_chats_list_to_chat,
                bundleOf(ChatFragment.CHAT_ID to id)
            )
        }
    }

    companion object {
        private const val POSITION = "position"
        fun newInstance(position: Int) = ChatsPageFragment().apply {
            arguments = bundleOf(POSITION to position)
        }
    }
}