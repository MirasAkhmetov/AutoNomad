package com.autonomad.ui.check_auto.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.check_auto.CarHistories
import com.autonomad.databinding.ItemCheckAutoHistoriesBinding


class HistoriesAdapter(private val data: List<CarHistories>) : RecyclerView.Adapter<HistoriesAdapter.HistoriesAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoriesAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemCheckAutoHistoriesBinding.inflate(inflater, parent, false)
        return HistoriesAdapterViewHolder(
            dataBinding
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: HistoriesAdapterViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class HistoriesAdapterViewHolder(private val dataBinding: ItemCheckAutoHistoriesBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(histories: CarHistories) {
            dataBinding.setVariable(BR.itemData, histories)
            dataBinding.executePendingBindings()
        }
    }
}