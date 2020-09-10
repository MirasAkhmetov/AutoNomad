package com.autonomad.ui.check_auto.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.check_auto.CarDtps
import com.autonomad.databinding.ItemCheckAutoDtpsBinding

class DtpAdapter(private val data: List<CarDtps>) : RecyclerView.Adapter<DtpAdapter.DtpAdapterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DtpAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemCheckAutoDtpsBinding.inflate(inflater, parent, false)
        return DtpAdapterViewHolder(
            dataBinding
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DtpAdapterViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class DtpAdapterViewHolder(private val dataBinding: ItemCheckAutoDtpsBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(carDtps: CarDtps) {
            dataBinding.setVariable(BR.itemData, carDtps)
            dataBinding.executePendingBindings()
        }
    }
}