package com.autonomad.ui.login.login_page

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(manager: FragmentManager?, val tabCount: Int) :
    FragmentStatePagerAdapter(manager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                LoginPage()
            }
            1 -> {
                RegistrationPage()
            }
            else -> LoginPage()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}