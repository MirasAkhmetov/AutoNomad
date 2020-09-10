package com.autonomad.ui.main_page.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.autonomad.data.models.meteo_currency.City

class FuelAdapter(fm: FragmentManager, private val fuels: City) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    companion object {
        private val marks = listOf("АИ-92", "АИ-95", "АИ-98", "ДТ", "ДТЗ", "Газ")
    }

    override fun getItem(position: Int): Fragment {
        val t = Triple(fuels.marks[0][position], fuels.marks[1][position], fuels.marks[2][position])
        return FuelFragment.newInstance(t)
    }

    override fun getCount() = marks.size
}