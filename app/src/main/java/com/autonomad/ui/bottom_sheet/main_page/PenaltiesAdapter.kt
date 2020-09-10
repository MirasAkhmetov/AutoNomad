package com.autonomad.ui.bottom_sheet.main_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.data.models.penalty.Driver
import com.autonomad.databinding.ItemMainPagePenaltyBinding

class PenaltiesAdapter : RecyclerView.Adapter<PenaltiesAdapter.PenaltyViewHolder>() {
    var drivers: List<Driver> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PenaltyViewHolder(
        ItemMainPagePenaltyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = drivers.size

    override fun onBindViewHolder(holder: PenaltyViewHolder, position: Int) {
        holder.binding.driver = drivers[position]
    }

    class PenaltyViewHolder(val binding: ItemMainPagePenaltyBinding) : RecyclerView.ViewHolder(binding.root)
}