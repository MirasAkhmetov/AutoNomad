package com.autonomad.ui.claims.user_claims.home.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.claims.ReviewFilter
import com.autonomad.data.models.claims.getBackground
import com.autonomad.data.models.claims.getTextColor
import kotlinx.android.synthetic.main.item_claims_user_type_of_categories.view.*

class FeedbackTypeAdapter(
    var types: List<ReviewFilter>,
    val onFilterSelected: (Int, ReviewFilter.Types) -> Unit
) :
    RecyclerView.Adapter<FeedbackTypeAdapter.FeedbackTypeAdapterViewHolder>() {

    class FeedbackTypeAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(filter: ReviewFilter, onFilterSelected: (Int, ReviewFilter.Types) -> Unit) {
            itemView.types.apply {
                text = filter.name
                background = ContextCompat.getDrawable(itemView.context, filter.getBackground())
                setTextColor(ContextCompat.getColor(itemView.context, filter.getTextColor()))
                setOnClickListener { onFilterSelected(adapterPosition, filter.type) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackTypeAdapterViewHolder {
        return FeedbackTypeAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_claims_user_type_of_categories,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return types.size
    }

    override fun onBindViewHolder(holder: FeedbackTypeAdapterViewHolder, position: Int) {
        holder.bind(types[position], onFilterSelected)
    }

    fun updateItems(position: Int) {
        types.forEach {
            it.isSelected = it.id == position
        }
        notifyDataSetChanged()
    }
}