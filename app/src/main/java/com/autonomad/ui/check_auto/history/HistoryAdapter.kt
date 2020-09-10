package com.autonomad.ui.check_auto.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.check_auto.CheckAutoHistory
import com.autonomad.databinding.ItemCheckAutoHistoryBinding

class HistoryAdapter(
    private val list: List<CheckAutoHistory>,
    private val openTicketDetails: (id: Int) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemCheckAutoHistoryBinding.inflate(inflater, parent, false)
        return HistoryViewHolder(dataBinding, openTicketDetails)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class HistoryViewHolder(
        private val dataBinding: ItemCheckAutoHistoryBinding,
        private val openTicketDetails: (id: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(checkAutoHistory: CheckAutoHistory) {
            dataBinding.setVariable(BR.itemData, checkAutoHistory)
            dataBinding.container.setOnClickListener {
                openTicketDetails.invoke(checkAutoHistory.id)
            }

            dataBinding.executePendingBindings()
        }
    }
}