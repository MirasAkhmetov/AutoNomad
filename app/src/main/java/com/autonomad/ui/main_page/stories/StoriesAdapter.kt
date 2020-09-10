package com.autonomad.ui.main_page.stories

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.autonomad.data.models.Story

class StoriesAdapter(manager: FragmentManager, private val stories: List<Story>) :
    FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return stories.size
    }

    override fun getItem(position: Int): Fragment {
        return StoryFragment.newInstance(position)
    }
}