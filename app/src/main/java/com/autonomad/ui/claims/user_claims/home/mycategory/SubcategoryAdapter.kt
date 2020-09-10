package com.autonomad.ui.claims.user_claims.home.mycategory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.claim_user.SubCategory
import com.autonomad.databinding.ItemClaimsSpecialSubcategoryBinding

class SubcategoryAdapter : RecyclerView.Adapter<SubcategoryAdapter.SubcategoryViewHolder>() {

    var subCategories: List<SubCategory> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubcategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemClaimsSpecialSubcategoryBinding.inflate(inflater, parent, false)
        return SubcategoryViewHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return subCategories.size
    }

    override fun onBindViewHolder(holder: SubcategoryViewHolder, position: Int) {
        holder.bind(subCategories[position])
    }

    class SubcategoryViewHolder constructor(private val dataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(subCategory: SubCategory) {
            dataBinding.setVariable(BR.itemData, subCategory)
            dataBinding.executePendingBindings()
        }
    }

    fun updateRepoList(subCategories: List<SubCategory>) {
        this.subCategories = subCategories
        notifyDataSetChanged()
    }
}