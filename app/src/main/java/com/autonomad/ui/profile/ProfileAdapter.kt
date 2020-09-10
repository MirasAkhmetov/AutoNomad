package com.autonomad.ui.profile

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.autonomad.ui.profile.avto_profile.CarsPage
import com.autonomad.ui.profile.driver_profile.DriversPage

class ProfileAdapter(manager: FragmentManager?, private val tabCount: Int) :
    FragmentStatePagerAdapter(manager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = if (position == 0) CarsPage() else DriversPage()

    override fun getCount() = tabCount
}
