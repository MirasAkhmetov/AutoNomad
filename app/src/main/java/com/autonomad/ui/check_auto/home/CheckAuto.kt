package com.autonomad.ui.check_auto.home

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.ui.bottom_sheet.check_auto.CheckAutoBottomDialog
import com.autonomad.ui.check_auto.history_list.CheckAutoHistoryListFragment
import com.autonomad.ui.insurance.insurance_home.InsurancePagerAdapter
import com.autonomad.utils.navigateBack
import com.autonomad.utils.toast
import kotlinx.android.synthetic.main.fragment_check_auto_home.*
import kotlinx.android.synthetic.main.fragment_check_auto_home.main_content
import kotlinx.android.synthetic.main.fragment_check_auto_home.progress_bar
import kotlinx.android.synthetic.main.fragment_check_auto_home.tab_layout
import kotlinx.android.synthetic.main.fragment_check_auto_home.view_pager
import org.koin.android.viewmodel.ext.android.viewModel

class CheckAuto : Fragment() {
    private val checkAutoViewModel: CheckAutoViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_check_auto_home, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.check_auto_home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.link_to_guide) {
            findNavController().navigate(R.id.action_check_auto_home_to_checkAutoInfo)
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        initialise()
        setObservers()

        checkAutoViewModel.loadGarageCars()
        checkAutoViewModel.loadGarageCarsOrders()
    }

    private fun initialise() {
        navigateBack(R.id.action_global_services)


        val adapter = InsurancePagerAdapter(childFragmentManager)
        adapter.addFragment(
            CheckAutoHistoryListFragment.newInstance(
                garageAutoList = checkAutoViewModel.garageAutoList, isGaragePage = true
            ),
            "Автомобили"
        )
        adapter.addFragment(
            CheckAutoHistoryListFragment.newInstance(
                ordersList = checkAutoViewModel.ordersList, isGaragePage = false
            ),
            "История проверки"
        )
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)

        layout_add_car.setOnClickListener {
            CheckAutoBottomDialog(::onVinFound).show(
                childFragmentManager,
                "AddCarFromCheckAutoDialog"
            )
        }
    }

    private fun onVinFound(vin: String) {
        val args = bundleOf("vin" to vin)
        findNavController().navigate(R.id.action_check_auto_home_to_report, args)
//        checkAutoViewModel.loadGarageCars()
//        checkAutoViewModel.loadGarageCarsOrders()
    }

    private fun setObservers() {
        checkAutoViewModel.toast.observe(viewLifecycleOwner, Observer {
            toast(it)
        })
        checkAutoViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progress_bar.visibility = View.VISIBLE
                main_content.visibility = View.GONE
            } else {
                progress_bar.visibility = View.GONE
                main_content.visibility = View.VISIBLE
            }
        })
    }
}
