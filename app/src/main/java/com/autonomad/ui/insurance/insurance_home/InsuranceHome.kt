package com.autonomad.ui.insurance.insurance_home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.autonomad.R
import com.autonomad.ui.alert_penalty.AlertPenalty
import com.autonomad.ui.bottom_sheet.insurance.CheckPolis
import com.autonomad.ui.insurance.insurance_list.InsuranceListFragment
import com.autonomad.utils.navigateBack
import com.autonomad.utils.toast
import kotlinx.android.synthetic.main.fragment_insurance_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class InsuranceHome : Fragment() {
    private val insuranceHomeViewModel: InsuranceHomeViewModel by viewModel()
    private var sliders = intArrayOf(
        R.drawable.ic_banner1,
        R.drawable.ic_banner2,
        R.drawable.ic_banner3
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_insurance_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        observe()
        onClickListener()

        update()
    }

    private fun observe() {
        insuranceHomeViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progress_bar.visibility = VISIBLE
                main_content.visibility = GONE
            } else {
                progress_bar.visibility = GONE
                main_content.visibility = VISIBLE
            }
        })
        insuranceHomeViewModel.showDriverDeletedDialog.observe(viewLifecycleOwner, Observer {
            if (it) showSuccesfullyDriverDeleted()
        })
        insuranceHomeViewModel.errorMsg.observe(viewLifecycleOwner, Observer {
            toast(it)
        })
    }

    private fun onClickListener() {
        add.setOnClickListener {
            val args = Bundle()
            args.putInt("dest", R.id.action_insurance_home_to_insurance_home)
            CheckPolis.show(childFragmentManager, args)
        }
    }

    private fun update() {
        insuranceHomeViewModel.update()
    }

    private fun initialise() {
        val adapter = InsurancePagerAdapter(childFragmentManager)
        adapter.addFragment(
            InsuranceListFragment.newInstance(
                insuranceHomeViewModel.insurancesFavourite
            ),
            getString(R.string.my_insurances)
        )
        adapter.addFragment(
            InsuranceListFragment.newInstance(
                insuranceHomeViewModel.insurancesHistory
            ),
            getString(R.string.check_history)
        )
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)

        arguments?.let {
            val toHistory = arguments?.getBoolean("toHistory")
            if (toHistory != null && toHistory) {
                view_pager.setCurrentItem(1)
            }

        }

        navigateBack(R.id.action_global_services)

        carouselView.apply {
            setImageListener { position, imageView ->
                imageView.setImageResource(sliders[position])
                imageView.scaleType = ImageView.ScaleType.FIT_XY
            }
            pageCount = sliders.size
        }
    }

    private fun showSuccesfullyDriverDeleted() {
        val alert = AlertPenalty(requireContext())
        alert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alert.show()
    }
}