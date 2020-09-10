package com.autonomad.ui.claims.specialist_claims.feedback

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.autonomad.R
import com.autonomad.utils.navigateBack
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_claims_specialist_feedback.*

class Feedback : Fragment(R.layout.fragment_claims_specialist_feedback) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())
        val viewPagerAdapter = FeedbackPageAdapter(childFragmentManager, tabLayout.tabCount)
        pager.offscreenPageLimit = 2
        pager.adapter = viewPagerAdapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setupWithViewPager(pager)
        tabLayout.getTabAt(0)?.text = "Отклики"
        tabLayout.getTabAt(1)?.text = "Активные"
        tabLayout.getTabAt(2)?.text = "Завершенные"
        navigateBack(R.id.action_claim_specialist_feedback_to_home)

        arguments?.getInt(SELECTED_TAB)?.let {
            if (it != 0) pager.currentItem = it
        }
    }

    companion object {
        const val SELECTED_TAB = "selectedTab"
    }
}