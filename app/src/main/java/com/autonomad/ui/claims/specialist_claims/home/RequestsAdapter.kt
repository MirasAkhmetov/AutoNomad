package com.autonomad.ui.claims.specialist_claims.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.claims.ServiceRequest
import com.autonomad.utils.*
import kotlinx.android.synthetic.main.item_claims_specalist_claim_category.view.*
import kotlinx.android.synthetic.main.item_claims_specialist_claims.view.*

class RequestsAdapter(override var scrollListener: ScrollListener? = null) :
    PagedAdapter<ServiceRequest, RequestsAdapter.RequestViewHolder>(RequestDiffUtil()) {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun getItemViewHolder(parent: ViewGroup, viewType: Int) =
        RequestViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_claims_specialist_claims, parent, false))

    override fun onBindViewHolder(holder: PagingViewHolder, position: Int) {
        if (holder is LoadingViewHolder) return
        val r = getItem(position)
        holder.itemView.apply {
            tv_title.text = r.description
            tv_date.text = r.time
            tv_price.text = if (r.budget != null) context.getString(R.string.price, r.budget.toString()) else
                context.getString(R.string.null_price)
            val adapter = ClaimCategoriesAdapter().apply { items = r.subcategories.map { it.name } }
            if (r.images.isNotEmpty()) iv_photo.loadImage(r.images[0].thumbnailFull)
            rv_categories.adapter = adapter
            rv_categories.setRecycledViewPool(viewPool)
        }
    }

    class RequestViewHolder(view: View) : PagingViewHolder(view)

    private class RequestDiffUtil : DiffUtil.ItemCallback<ServiceRequest>() {
        override fun areItemsTheSame(oldItem: ServiceRequest, newItem: ServiceRequest) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ServiceRequest, newItem: ServiceRequest) = oldItem == newItem
    }
}

class ClaimCategoriesAdapter : RecyclerView.Adapter<ClaimCategoriesAdapter.ClaimCategoriesViewHolder>() {
    var items = emptyList<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var expanded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ClaimCategoriesViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_claims_specalist_claim_category, parent, false)
    )

    override fun getItemCount(): Int {
        if (items.size < 4) expanded = true
        return if (expanded) items.size else 3
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ClaimCategoriesViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.tv_category.apply {
            if (expanded || position < 2) {
                text = item
                background = context.getDrawable(R.drawable.background_progress_8dp_rectangle)
            } else {
                text = "+ ещё ${items.size - 2}"
                background = null
                setOnClickListener {
                    expanded = true
                    notifyDataSetChanged()
                }
            }
        }
    }

    class ClaimCategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view)
}