package com.autonomad.ui.claims.specialist_claims.home.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.claims.Subcategory
import kotlinx.android.synthetic.main.item_claims_specialist_subcategory.view.*

class SubcategoryAdapter : RecyclerView.Adapter<SubcategoryAdapter.SubcategoryViewHolder>() {
    var items = emptyList<Subcategory>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SubcategoryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_claims_specialist_subcategory, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SubcategoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class SubcategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Subcategory) {
            itemView.tv_subcategory.text = item.name
        }
    }
}