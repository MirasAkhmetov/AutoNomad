package com.autonomad.ui.insurance.insurance_home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.R
import com.autonomad.data.models.insurance.InsuranceHistory
import com.autonomad.databinding.ItemInsuranceDriverBinding
import com.autonomad.ui.bottom_sheet.insurance.DeleteDriverDialog
import kotlinx.android.synthetic.main.item_insurance_driver.view.*

class InsurersAdapter(
    private val drivers: List<InsuranceHistory>,
    private val context: Context,
    private val showMoreDriverCallback: (id: Int) -> Unit
) : RecyclerView.Adapter<InsurersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsurersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemInsuranceDriverBinding.inflate(inflater, parent, false)
        return InsurersViewHolder(dataBinding)
    }

    override fun getItemCount() = drivers.size
    override fun onBindViewHolder(holder: InsurersViewHolder, position: Int) {
        holder.bind(drivers[position])

        //delete button
        val bottomSheet = DeleteDriverDialog(object : DeleteDriverDialog.DeleteDriverAction {
            override fun deleteClicked() {
                //
            }
        }, context)
        bottomSheet.window
            ?.findViewById<View>(R.id.design_bottom_sheet)
            ?.setBackgroundResource(android.R.color.transparent);
        holder.itemView.properties.setOnClickListener {
            bottomSheet.show()
        }
        //update button
        holder.itemView.update.setOnClickListener {
            //updateCallback.invoke()
            startRefreshAnimation(holder.itemView.update)
        }

        holder.itemView.moreButton.setOnClickListener {
            showMoreDriverCallback.invoke(drivers[position].id)
        }

        var expanded = false
        holder.itemView.arrowClickableArea.setOnClickListener {
            expanded = !expanded
            holder.itemView.iv_dropdown.animate().rotationBy(if (expanded) 180F else -180F)
                .setDuration(200).withEndAction {
                    if (expanded)
                        holder.itemView.iv_dropdown.rotation = 180F
                    else
                        holder.itemView.iv_dropdown.rotation = 0F
                }.start()
            if (expanded) {
                holder.itemView.details.visibility = VISIBLE
                holder.itemView.status_ll.visibility = GONE
            } else {
                holder.itemView.details.visibility = GONE
                holder.itemView.status_ll.visibility = VISIBLE
            }
        }
    }

    private fun startRefreshAnimation(view: View) {
        view.animate().rotationBy(30f).setDuration(60L).withEndAction {
            startRefreshAnimation(view) //todo lapwa madi
//            if (refreshIndicators[indicator] == true) startRefreshAnimation(view, indicator)
//            else view.rotation = 0f
        }.start()
    }
}

class InsurersViewHolder(private val binding: ItemInsuranceDriverBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(insuranceHistory: InsuranceHistory) {
        binding.setVariable(BR.itemData, insuranceHistory)
        binding.executePendingBindings()
    }

}