package com.autonomad.ui.penalty.penalties_of_person

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.penalty.Result
import com.autonomad.databinding.ItemPenaltyInfoBinding

class PersonPenaltyAdapter : RecyclerView.Adapter<PersonPenaltyAdapter.PersonPenaltyViewHolder>() {
    var penalties: List<Result> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonPenaltyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PersonPenaltyViewHolder(ItemPenaltyInfoBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = penalties.size

    override fun onBindViewHolder(holder: PersonPenaltyViewHolder, position: Int) {
        holder.bind(penalties[position])
    }

    class PersonPenaltyViewHolder(private val dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(personPenalty: Result) {
            dataBinding.setVariable(BR.itemData, personPenalty)
            dataBinding.executePendingBindings()
        }
    }
}
