package com.autonomad.ui.main_page.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.autonomad.R
import com.autonomad.data.models.meteo_currency.Mark
import kotlinx.android.synthetic.main.fragment_fuel.*

class FuelFragment : Fragment(R.layout.fragment_fuel) {
    companion object {
        fun newInstance(fuelList: Triple<Mark, Mark, Mark>) = FuelFragment().apply {
            arguments = bundleOf("first" to fuelList.first, "second" to fuelList.second, "third" to fuelList.third)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getParcelable<Mark>("first")?.let {
            setIcon(it.markName, iv_first)
            lbl_first.text = it.name
            tv_first.text = it.price
        }
        arguments?.getParcelable<Mark>("second")?.let {
            setIcon(it.markName, iv_second)
            lbl_second.text = it.name
            tv_second.text = it.price
        }
        arguments?.getParcelable<Mark>("third")?.let {
            setIcon(it.markName, iv_third)
            lbl_third.text = it.name
            tv_third.text = it.price
        }
    }

    private fun setIcon(company: String, view: ImageView) {
        view.setImageResource(
            when (company) {
                "КазМунайГаз" -> R.drawable.ic_azs_kmg
                "Royal Petrol" -> R.drawable.ic_azs_rp
                "Helios" -> R.drawable.ic_azs_helios
                "Аурика" -> R.drawable.ic_azs_aurika
                "NRG" -> R.drawable.ic_azs_nrg
                "Compass" -> R.drawable.ic_azs_compass
                "TS Oil" -> R.drawable.ic_azs_ts
                "Bip" -> R.drawable.ic_azs_bip
                "Etalon Auto" -> R.drawable.ic_azs_etalon
                "Seda" -> R.drawable.ic_azs_seda
                "ГазOilПром" -> R.drawable.ic_azs_gazoil
                "Конденсат АЗС" -> R.drawable.ic_azs_condensat
                "Газпромнефть" -> R.drawable.ic_azs_gazprom
                "Petrol Asia" -> R.drawable.ic_azs_petrolasia
                "V-Oil" -> R.drawable.ic_azs_v_oil
                "PEGAS" -> R.drawable.ic_azs_pegas
                "PETROLUX" -> R.drawable.ic_azs_petrolux
                else -> return
            }
        )
    }
}