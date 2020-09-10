package com.autonomad.ui.bottom_sheet.main_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.data.models.insurance.InsuranceHistory
import com.autonomad.databinding.ItemMainPageInsuranceBinding

class InsuranceAdapter : RecyclerView.Adapter<InsuranceAdapter.InsuranceViewHolder>() {
    var items: List<InsuranceHistory> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        InsuranceViewHolder(ItemMainPageInsuranceBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: InsuranceViewHolder, position: Int) {
        val insurance = items[position]
        holder.binding.insurance = insurance
    }

    class InsuranceViewHolder(val binding: ItemMainPageInsuranceBinding) : RecyclerView.ViewHolder(binding.root)
}