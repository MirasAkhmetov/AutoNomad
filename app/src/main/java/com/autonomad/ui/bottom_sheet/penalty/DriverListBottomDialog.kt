package com.autonomad.ui.bottom_sheet.penalty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.data.models.penalty.Driver
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.ui.penalty.penalty_premium.DriverAdapter
import com.autonomad.utils.Status
import kotlinx.android.synthetic.main.bottom_sheet_penalty_driver_list.*

class DriverListBottomDialog(private val drivers: LiveData<Status<List<Driver>>>, private val onDriverClick: (Int) -> Unit) :
    RoundBottomDialog() {

    private val adapter = DriverAdapter(deletable = false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_penalty_driver_list, container, false)
        setExpanded()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter.onDriverClick = {
            onDriverClick(it)
            dismiss()
        }
        rv_drivers.adapter = adapter

        drivers.observe(viewLifecycleOwner) {
            it.onSuccess {
                forEach { driver -> adapter.addDriver(driver) }
            }
        }
    }
}