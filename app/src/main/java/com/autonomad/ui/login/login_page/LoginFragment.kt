package com.autonomad.ui.login.login_page

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.ui.MainActivity
import com.autonomad.utils.Methods
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.login_page.*

class LoginFragment : Fragment(R.layout.login_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, tabLayout.tabCount)
        pager.adapter = viewPagerAdapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setupWithViewPager(pager)
        tabLayout.getTabAt(0)?.text = "Вход"
        tabLayout.getTabAt(1)?.text = "Регистрация"

        if (Methods.key.isNotEmpty())
            findNavController().navigate(R.id.main_page, bundleOf(MainActivity.MESSAGE to MainActivity.SET_MAIN))
    }
}
