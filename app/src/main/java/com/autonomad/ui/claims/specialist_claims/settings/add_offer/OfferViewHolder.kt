package com.autonomad.ui.claims.specialist_claims.settings.add_offer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import kotlinx.android.synthetic.main.item_claims_specialist_offer_category.view.*
import kotlinx.android.synthetic.main.item_claims_specialist_offer_subcategory.view.*
import com.autonomad.data.models.claims.Category as CategoryModel
import com.autonomad.data.models.claims.Subcategory as SubcategoryModel

sealed class OfferViewHolder<T : OnOfferClickListener>(parent: ViewGroup, @LayoutRes layout: Int) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false)) {

    abstract fun bind(isChecked: Boolean, listener: T)

    class Category(parent: ViewGroup) :
        OfferViewHolder<OnCategoryClick>(parent, R.layout.item_claims_specialist_offer_category) {
        private lateinit var item: CategoryModel

        override fun bind(isChecked: Boolean, listener: OnCategoryClick) {
            itemView.apply {
                tv_name.text = item.name
                iv_checked_category.isVisible = isChecked
                setOnClickListener {
                    listener.onClick(item)
                }
            }
        }

        fun setItem(newItem: CategoryModel) {
            item = newItem
        }
    }

    class Subcategory(parent: ViewGroup) :
        OfferViewHolder<OnSubcategoryClick>(parent, R.layout.item_claims_specialist_offer_subcategory) {
        private lateinit var item: SubcategoryModel
        private lateinit var listener: OnSubcategoryClick

        override fun bind(isChecked: Boolean, listener: OnSubcategoryClick) {
            this.listener = listener
            itemView.apply {
                cb_name.text = item.name
                cb_name.isChecked = isChecked
                group_if_checked.isVisible = isChecked
                cb_name.setOnClickListener {
                    if (!cb_name.isChecked) tv_price.text.clear()
                    listener.onCheck(cb_name.isChecked, item)
                }
                tv_price.doAfterTextChanged { text ->
                    listener.onPriceSet(text.toString().toIntOrNull(), item)
                    cb_name.setButtonDrawable(
                        if (text?.toString().orEmpty().isNotEmpty()) R.drawable.checkbox_round_light
                        else R.drawable.checkbox_round
                    )
                }
            }
        }

        fun setItem(newItem: SubcategoryModel, price: Int) {
            item = newItem
            itemView.tv_price.setText(if (price > 0) price.toString() else "")
        }
    }
}

interface OnOfferClickListener

interface OnCategoryClick : OnOfferClickListener {
    fun onClick(item: CategoryModel)
}

interface OnSubcategoryClick : OnOfferClickListener {
    fun onCheck(isChecked: Boolean, item: SubcategoryModel)

    fun onPriceSet(price: Int?, item: SubcategoryModel)
}