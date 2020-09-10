package com.autonomad.ui.penalty.penalty_home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.R
import com.autonomad.data.models.claims.PluralForms
import com.autonomad.data.models.claims.getPlural
import com.autonomad.data.models.penalty.Driver
import com.autonomad.databinding.ItemPenaltyDriverBinding
import kotlinx.android.synthetic.main.item_penalty_driver.view.*

class DriverAdapter(private val onDeleteClick: (Int) -> Unit, private val onRefresh: () -> Unit) :
    RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        instance = this
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        instance = null
    }

    var drivers: List<Driver> = emptyList()
        set(value) {
            field = value
            isRefreshing = false
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemPenaltyDriverBinding.inflate(inflater, parent, false)
        return DriverViewHolder(dataBinding, onDeleteClick, onRefresh)
    }

    override fun getItemCount() = drivers.size

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        holder.bind(drivers[position])
    }

    class DriverViewHolder(
        private val dataBinding: ViewDataBinding,
        private val onDeleteClick: (Int) -> Unit,
        private val onStartRefresh: () -> Unit
    ) : RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(driver: Driver) {
            dataBinding.setVariable(BR.itemData, driver)
            dataBinding.executePendingBindings()

            dataBinding.root.apply {
                properties.setOnClickListener {
                    onDeleteClick(driver.id)
                }

                penalty_info.text =
                    getPlural(driver.penaltyCount, PluralForms("штраф", "штрафа", "штрафов"), false)

                check.setOnClickListener {
                    val bundle = bundleOf("UIN" to driver.target)
                    it.findNavController().navigate(R.id.action_penalty_to_penaltiesOfPerson, bundle)
                }
                if (isRefreshing) iv_refresh.rotate()

                iv_refresh.setOnClickListener {
                    if (isRefreshing) return@setOnClickListener
                    onStartRefresh()
                    isRefreshing = true
                    it.rotate()
                }

                setOnClickListener { check.callOnClick() }
            }
        }

        private fun View.rotate() {
            animate().rotationBy(30f).setDuration(60).withEndAction {
                if (isRefreshing) rotate() else rotation = 0f
            }.start()
        }
    }

    companion object {
        var isRefreshing = false
            set(value) {
                field = value
                instance?.notifyDataSetChanged()
            }
        private var instance: DriverAdapter? = null
    }
}
