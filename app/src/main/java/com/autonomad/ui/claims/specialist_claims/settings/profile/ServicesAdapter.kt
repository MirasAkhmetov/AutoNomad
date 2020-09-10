package com.autonomad.ui.claims.specialist_claims.settings.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.claims.Category
import com.autonomad.data.models.claims.ServiceOffer
import kotlinx.android.synthetic.main.item_claims_specialist_profile_service.view.*
import kotlinx.android.synthetic.main.item_claims_specialist_profile_subcategory.view.*

class ServicesAdapter(private val onEditClick: (ServiceOffer) -> Unit) :
    RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>() {

    private var categories: List<Category> = emptyList()
    var items: List<ServiceOffer> = emptyList()
        set(value) {
            field = value
            categories = value.map { it.subcategory.category }.distinctBy { it.id }.sortedBy { it.id }
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ServiceViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_claims_specialist_profile_service, parent, false)
    )

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category, items.filter { it.subcategory.category.id == category.id }, onEditClick)
    }

    class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var expanded = false
        fun bind(item: Category, list: List<ServiceOffer>, onEditClick: (ServiceOffer) -> Unit) {
            itemView.apply {
                tv_category.text = item.name
                rv_subitems.adapter = SubcategoryAdapter(list, onEditClick)
                layout_main.setOnClickListener {
                    expanded = !expanded
                    iv_expand_arrow.animate().rotationBy(if (expanded) 180F else -180F).setDuration(200).start()
                    rv_subitems.isVisible = expanded
                }
            }
        }
    }
}

private class SubcategoryAdapter(var items: List<ServiceOffer>, private val onEditClick: (ServiceOffer) -> Unit) :
    RecyclerView.Adapter<SubcategoryAdapter.SubcategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SubcategoryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_claims_specialist_profile_subcategory, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SubcategoryViewHolder, position: Int) {
        holder.bind(items[position], onEditClick)
    }

    private class SubcategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ServiceOffer, onEditClick: (ServiceOffer) -> Unit) {
            itemView.apply {
                tv_name.text = item.subcategory.name
                tv_price.isVisible = item.price != null && item.price != 0
                tv_price.text = context.getString(R.string.price_from, item.price.toString())
                iv_edit.setOnClickListener { onEditClick(item) }
            }
        }
    }
}