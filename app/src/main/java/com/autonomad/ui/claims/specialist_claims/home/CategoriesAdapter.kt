package com.autonomad.ui.claims.specialist_claims.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.claims.Category
import com.autonomad.utils.loadImage
import kotlinx.android.synthetic.main.item_claims_specialist_categories.view.*

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {
    var items: List<Category> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoriesViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_claims_specialist_categories, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class CategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Category) {
            itemView.apply {
                tv_category.text = item.name
                tv_description.text = item.description.orEmpty()
                tv_count.isVisible = item.requestsCount != null && item.requestsCount > 0
                if (item.requestsCount != null && item.requestsCount > 0) tv_count.text = item.requestsCount.toString()
                item.image?.let { iv_icon.loadImage(it) }
            }
        }
    }
}