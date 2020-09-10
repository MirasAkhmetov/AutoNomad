package com.autonomad.ui.shop.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.ui.shop.model.PopularCategory
import kotlinx.android.synthetic.main.shop_item_popular_category.view.*

class PopularCategoryListAdapter(
    private val context: Context,
    private val list: List<PopularCategory>,
    private val onClick: (item: PopularCategory) -> Unit
) : RecyclerView.Adapter<PopularCategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.shop_item_popular_category, parent, false)
        return PopularCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PopularCategoryViewHolder, position: Int) {
        holder.itemView.title.text = list[position].title
        holder.itemView.cont.setOnClickListener {
            onClick.invoke(list[position])
        }
        //todo image set picasso
    }
}

class PopularCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view)