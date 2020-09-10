package com.autonomad.ui.claims.specialist_claims.feedback

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class FeedbackPageAdapter(manager: FragmentManager, private val tabCount: Int) :
    FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = FeedbackPage.newInstance(position)

    override fun getCount(): Int {
        return tabCount
    }
}