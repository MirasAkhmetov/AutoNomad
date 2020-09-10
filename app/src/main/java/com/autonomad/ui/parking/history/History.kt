package com.autonomad.ui.parking.history

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.data.models.parking.ParkingFilter
import com.autonomad.data.models.parking.ParkingOrder
import com.autonomad.databinding.FragmentParkingHistoryBinding
import com.autonomad.ui.bottom_sheet.Filter
import com.autonomad.utils.BaseFragment
import com.autonomad.utils.RecyclerItemClickListener
import com.autonomad.utils.navigateBack
import kotlinx.android.synthetic.main.fragment_parking_history.*
import java.util.*
import kotlin.collections.ArrayList

class History : BaseFragment() {
    private lateinit var ViewModel: FragmentParkingHistoryBinding
    private lateinit var parkingHistoryAdapter: HistoryAdapter
    private var filterType: ParkingFilter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        ViewModel = FragmentParkingHistoryBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProvider(this@History)
                .get(HistoryViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return ViewModel.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.parking_history_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.link_to_history_filter) {
            navigateToFilter()
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun initialise() {
        ViewModel.viewmodel?.getHistory()
        navigateBack(R.id.action_parking_history_to_parking)
    }

    override fun setObservers() {
        ViewModel.viewmodel?.data?.observe(viewLifecycleOwner, Observer {
            val list: ArrayList<ParkingOrder> = it as ArrayList<ParkingOrder>
            for (i in 0 until list.size) {
                list[i].updatedAt = list[i].updatedAt.substringBefore("T")
                when {
                    i == 0 -> list[i].show = true
                    list[i].updatedAt == list[i - 1].updatedAt -> list[i].show = false
                    else -> list[i].show = true
                }
            }
            parkingHistoryAdapter.updateRepoList(list.toMutableList())
        })
    }

    override fun setAdapter() {
        val viewModel = ViewModel.viewmodel
        if (viewModel != null) {
            parking_history_rv.layoutManager = LinearLayoutManager(context)
            parking_history_rv.setHasFixedSize(true)
            parkingHistoryAdapter = HistoryAdapter(ViewModel.viewmodel!!)
            parking_history_rv.adapter = parkingHistoryAdapter
            parking_history_rv.addOnItemTouchListener(
                RecyclerItemClickListener(parking_history_rv,
                    object : RecyclerItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            val bundle =
                                bundleOf("id" to ViewModel.viewmodel?.data?.value?.get(position)?.id.toString())
                            findNavController().navigate(
                                R.id.action_parking_history_to_detailTicketInformation,
                                bundle
                            )
                        }
                    })
            )
        }

    }

    override fun setOnClickListener() {
        text_month.setOnClickListener {
            navigateToFilter()
        }
    }

    private fun onFilterApplied(filter: ParkingFilter?) {
        filterType = filter
        ViewModel.viewmodel?.getHistory(filterType?.id)
        text_month.text = if (filter?.id == null) {
            getString(R.string.for_all_time)
        } else {
            filter.name.toLowerCase(Locale.getDefault())
        }

    }

    private fun navigateToFilter() {
        val filter = Filter(context as Context, filterType?.id, ::onFilterApplied)
        filter.window?.findViewById<View>(R.id.design_bottom_sheet)
            ?.setBackgroundResource(android.R.color.transparent)
        filter.show()
    }
}