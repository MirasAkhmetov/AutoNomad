package com.autonomad.ui.claims.specialist_claims.settings.add_offer

import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.claims.Category
import com.autonomad.data.models.claims.ServiceOffer
import com.autonomad.data.models.claims.Subcategory
import kotlinx.android.synthetic.main.fragment_claims_specialist_add_offer.view.*

class OfferAdapter(
    private val radioGroup: RadioGroup,
    private val onSelected: (List<ServiceOffer>) -> Unit
) : RecyclerView.Adapter<OfferViewHolder<*>>() {
    var items = emptyList<Category>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var selectedCategory: Category
    private val categorySelected: Boolean
        get() = ::selectedCategory.isInitialized


    private val selectedGroup: Int
        get() = when (radioGroup.checkedRadioButtonId) {
            R.id.rb_category -> 0
            R.id.rb_subcategory -> 1
            else -> -1
        }

    val selectedSubcategories = mutableListOf<ServiceOffer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (selectedGroup) {
        0 -> OfferViewHolder.Category(parent)
        1 -> OfferViewHolder.Subcategory(parent)
        else -> throw IndexOutOfBoundsException()
    }

    override fun getItemCount(): Int {
        val o = when {
            selectedGroup == 0 -> items.size
            selectedGroup == 1 && categorySelected -> selectedCategory.subcategories?.size ?: 0
            else -> 0
        }
        return o
    }

    override fun onBindViewHolder(holder: OfferViewHolder<*>, position: Int) {
        when (holder) {
            is OfferViewHolder.Category -> {
                val category = items[position]
                holder.setItem(category)
                holder.bind(categorySelected && selectedCategory == category, object : OnCategoryClick {
                    override fun onClick(item: Category) {
                        if (!categorySelected || selectedCategory != item) {
                            selectedCategory = item
                            selectedSubcategories.clear()
                            radioGroup.rb_subcategory.isEnabled = true
                        }
                        updateRadioButtonsTexts()
                        radioGroup.rb_category.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0, 0, R.drawable.ic_check_grey_3_12dp, 0
                        )
                        radioGroup.check(R.id.rb_subcategory)
                        notifyDataSetChanged()
                    }
                })
            }
            is OfferViewHolder.Subcategory -> {
                val subcategory = selectedCategory.subcategories!![position]
                holder.setItem(subcategory, isSelected(subcategory)?.price ?: 0)
                holder.bind(isSelected(subcategory) != null, object : OnSubcategoryClick {
                    override fun onCheck(isChecked: Boolean, item: Subcategory) {
                        if (isChecked) addOffer(ServiceOffer(subcategory = item), true)
                        else selectedSubcategories.removeAll { it.subcategory == item }
                        onSelected(selectedSubcategories)
                        updateRadioButtonsTexts()
                        notifyDataSetChanged()
                    }

                    override fun onPriceSet(price: Int?, item: Subcategory) {
                        addOffer(ServiceOffer(subcategory = item, price = price ?: 0))
                    }
                })
            }
        }
    }

    private fun addOffer(offer: ServiceOffer, force: Boolean = false) {
        if (selectedSubcategories.removeAll { it.subcategory == offer.subcategory } || force)
            selectedSubcategories.add(offer)
    }

    private fun isSelected(subcategory: Subcategory) = selectedSubcategories.firstOrNull { it.subcategory == subcategory }

    private fun updateRadioButtonsTexts() {
        radioGroup.rb_category.text = if (categorySelected) selectedCategory.name else "Услуга"
        radioGroup.rb_subcategory.text = when (selectedSubcategories.size) {
            0 -> "Категория"
            1 -> selectedSubcategories[0].subcategory.name
            else -> selectedSubcategories[0].subcategory.name + ", +" + (selectedSubcategories.size - 1)
        }
    }

    override fun getItemViewType(position: Int) = selectedGroup
}