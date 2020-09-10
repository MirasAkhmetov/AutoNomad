package com.autonomad.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R

/**
 *  Do not use with NestedScrollView
 */
abstract class PagedAdapter<T, VH : PagingViewHolder>(
    itemCallback: DiffUtil.ItemCallback<T>,
    private val threshold: Int = 3
) : ListAdapter<T, PagingViewHolder>(itemCallback) {

    abstract var scrollListener: ScrollListener?
    private var isLoading = false
    private var isFinished = false

    var items: List<T>
        get() = currentList
        set(value) {
            submitList(value)
            setLoaded()
        }

    private lateinit var rv: RecyclerView
    private val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = (recyclerView.layoutManager as? LinearLayoutManager) ?: return
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            if (!isLoading && lastVisibleItemPosition + threshold >= layoutManager.itemCount) {
                isLoading = true
                scrollListener?.loadMore()
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        rv = recyclerView
        rv.addOnScrollListener(listener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        finish()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingViewHolder =
        if (viewType != VIEW_TYPE_LOADING) getItemViewHolder(parent, viewType) else LoadingViewHolder(parent)

    override fun getItemCount() = items.size + if (isFinished) 0 else 1

    override fun getItemViewType(position: Int) = if (position == items.size) VIEW_TYPE_LOADING else getItemType(position)

    fun setLoaded() {
        isLoading = false
    }

    fun finish() {
        isFinished = true
        notifyDataSetChanged()
        rv.removeOnScrollListener(listener)
    }

    protected open fun getItemType(position: Int) = VIEW_TYPE_ITEM

    abstract fun getItemViewHolder(parent: ViewGroup, viewType: Int): VH

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = -1
    }
}

abstract class PagingViewHolder(view: View) : RecyclerView.ViewHolder(view)

class LoadingViewHolder(parent: ViewGroup) :
    PagingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false))

interface ScrollListener {
    fun loadMore()
}