package com.autonomad.ui.claims.specialist_claims.feedback

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.autonomad.BR
import com.autonomad.data.models.claims.ServiceResponse
import com.autonomad.databinding.ItemClaimsSpecialistFeedbackBinding
import com.autonomad.utils.PagedAdapter
import com.autonomad.utils.PagingViewHolder
import com.autonomad.utils.ScrollListener
import kotlinx.android.synthetic.main.item_claims_specialist_feedback.view.*

class FeedbackAdapter(private val status: Int, override var scrollListener: ScrollListener?) :
    PagedAdapter<ServiceResponse, FeedbackAdapter.FeedbackViewHolder>(ResponseDiff(), 4) {

    var listener: OnClickListener? = null

    override fun getItemViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FeedbackViewHolder(ItemClaimsSpecialistFeedbackBinding.inflate(inflater, parent, false), listener)
    }

    override fun onBindViewHolder(holder: PagingViewHolder, position: Int) {
        if (holder is FeedbackViewHolder) holder.bind(getItem(position), status)
    }

    class FeedbackViewHolder(private val binding: ViewDataBinding, private val listener: OnClickListener?) :
        PagingViewHolder(binding.root) {

        fun bind(item: ServiceResponse, status: Int) {
            binding.setVariable(BR.request, item.request)
            binding.setVariable(BR.status, status)
            binding.executePendingBindings()
            binding.root.iv_options.setOnClickListener { listener?.onOptionsClick(it, item.id) }
            binding.root.setOnClickListener { listener?.onItemClick(item.id) }
        }
    }

    interface OnClickListener {
        fun onOptionsClick(view: View, responseId: Int)
        fun onItemClick(responseId: Int)
    }

    private class ResponseDiff : DiffUtil.ItemCallback<ServiceResponse>() {
        override fun areItemsTheSame(oldItem: ServiceResponse, newItem: ServiceResponse) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ServiceResponse, newItem: ServiceResponse) = oldItem == newItem
    }
}