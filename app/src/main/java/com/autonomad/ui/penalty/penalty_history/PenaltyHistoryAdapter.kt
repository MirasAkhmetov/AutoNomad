package com.autonomad.ui.penalty.penalty_history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.penalty.PaymentHistory
import com.autonomad.databinding.ItemPenaltyHistoryBinding

class PenaltyHistoryAdapter : RecyclerView.Adapter<PenaltyHistoryAdapter.PenaltyHistoryViewHolder>() {

    var items: List<PaymentHistory> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenaltyHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PenaltyHistoryViewHolder(ItemPenaltyHistoryBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PenaltyHistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class PenaltyHistoryViewHolder(private val dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(history: PaymentHistory) {
            dataBinding.setVariable(BR.itemData, history)
            dataBinding.executePendingBindings()
        }
    }
}

