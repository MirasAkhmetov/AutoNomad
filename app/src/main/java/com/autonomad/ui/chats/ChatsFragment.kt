package com.autonomad.ui.chats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.autonomad.R
import com.autonomad.data.repo.chat.ChatRepo
import com.autonomad.utils.LifecycleObservers
import com.autonomad.utils.navigateBack
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_chats.*
import org.koin.android.ext.android.inject

class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private val observers: LifecycleObservers by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateBack(R.id.action_global_main_page)
        lifecycle.addObserver(observers.newObserver(javaClass.simpleName))
        view_pager.offscreenPageLimit = 2
        view_pager.adapter = ChatsPageAdapter(childFragmentManager)
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.setupWithViewPager(view_pager)
    }
}
