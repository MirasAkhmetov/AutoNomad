package com.autonomad.ui.penalty.penalty_premium

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.penalty.Driver
import kotlinx.android.synthetic.main.item_penalty_premium_driver.view.*

class DriverAdapter(private val deletable: Boolean = true) :
    RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {

    var onDriverAmountChange: ((Boolean) -> Unit)? = null
    var onDriverClick: ((Int) -> Unit)? = null

    private val drivers = mutableListOf<Driver>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DriverViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_penalty_premium_driver, parent, false))

    override fun getItemCount() = drivers.size

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        holder.itemView.apply {
            tv_driver.text = drivers[position].name
            if (deletable) iv_remove.setOnClickListener { removeDriver(position) }
            else {
                iv_remove.isVisible = false
                setOnClickListener { onDriverClick?.invoke(drivers[position].id) }
            }
        }
    }

    fun addDriver(driver: Driver) {
        if (drivers.map { it.id }.contains(driver.id)) return
        drivers.add(driver)
        if (drivers.size == 1) onDriverAmountChange?.invoke(true)
        notifyDataSetChanged()
    }

    private fun removeDriver(position: Int) {
        drivers.removeAt(position)
        if (drivers.size == 0) onDriverAmountChange?.invoke(false)
        notifyDataSetChanged()
    }

    class DriverViewHolder(view: View) : RecyclerView.ViewHolder(view)
}