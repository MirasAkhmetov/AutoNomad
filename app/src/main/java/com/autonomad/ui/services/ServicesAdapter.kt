package com.autonomad.ui.services

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ServicesAdapter(manager: FragmentManager?, val tabCount: Int) :
    FragmentStatePagerAdapter(manager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ServicesPage1()
            }
            1 -> {
                ServicesPage2()
            }

            else -> ServicesPage1()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}