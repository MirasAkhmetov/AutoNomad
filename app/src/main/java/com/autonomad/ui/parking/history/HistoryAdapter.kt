package com.autonomad.ui.parking.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.parking.ParkingOrder
import com.autonomad.databinding.ItemParkingHistoryBinding
import kotlinx.android.synthetic.main.item_penalty_history.view.*

class HistoryAdapter(private val HistoryViewModel: HistoryViewModel) :
    RecyclerView.Adapter<HistoryAdapter.PenaltyHistoryViewHolder>() {
    var history: List<ParkingOrder> = emptyList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PenaltyHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemParkingHistoryBinding.inflate(inflater, parent, false)
        return PenaltyHistoryViewHolder(dataBinding, HistoryViewModel)
    }

    override fun getItemCount(): Int {
        return history.size
    }

    override fun onBindViewHolder(
        holder: PenaltyHistoryViewHolder,
        position: Int
    ) {
        holder.bind(history[position])
    }

    class PenaltyHistoryViewHolder(
        private val dataBinding: ViewDataBinding,
        private val HistoryViewModel: HistoryViewModel
    ) : RecyclerView.ViewHolder(dataBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(history: ParkingOrder) {
            dataBinding.setVariable(BR.itemData, history)
            dataBinding.executePendingBindings()
            dataBinding.root.price.text = dataBinding.root.price.text.toString() + " ₸"
        }
    }

    fun updateRepoList(history: List<ParkingOrder>) {
        this.history = history
        notifyDataSetChanged()
    }
}