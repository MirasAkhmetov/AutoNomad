package com.autonomad.ui.chats

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.autonomad.ui.chats.page.ChatsPageFragment

class ChatsPageAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = 3

    override fun getItem(position: Int) = ChatsPageFragment.newInstance(position)

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "Общее"
        1 -> "Заявки"
        else -> "Магазин"
    }
}