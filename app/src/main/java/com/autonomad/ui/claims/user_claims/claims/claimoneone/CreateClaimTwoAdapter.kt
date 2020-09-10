package com.autonomad.ui.claims.user_claims.claims.claimoneone

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.data.models.claim_user.SubCategory
import com.autonomad.databinding.ItemClaimsCreateTwoBinding
import com.autonomad.ui.claims.user_claims.home.mycategory.CategoryListViewModel
import kotlinx.android.synthetic.main.item_claims_create_two.view.*


class CreateClaimTwoAdapter(private val categoryListViewModel: CategoryListViewModel,
                            val context: Context ) :
    RecyclerView.Adapter<CreateClaimTwoAdapter.CreateClaimTwoViewHolder>() {

    var subCategories: List<SubCategory> = emptyList()

     var stList: List<SubCategory> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CreateClaimTwoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemClaimsCreateTwoBinding.inflate(inflater, parent, false)
        stList = categoryListViewModel.subcategory.value!!
        return CreateClaimTwoViewHolder(dataBinding, categoryListViewModel, context)
    }

    override fun getItemCount(): Int {
        return subCategories.size
    }

    override fun onBindViewHolder(holder: CreateClaimTwoViewHolder, position: Int) {
        holder.bind(subCategories[position])

    }

    class CreateClaimTwoViewHolder constructor(
        private val dataBinding: ViewDataBinding,
        private val categoryListViewModel: CategoryListViewModel,
        val context: Context
    ) : RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(subCategory: SubCategory) {

            dataBinding.root.cb_check.text = subCategory.name
            dataBinding.root.cb_check.isChecked = subCategory.isfall
            dataBinding.root.cb_check.tag = subCategory

            dataBinding.root.cb_check.setOnClickListener(View.OnClickListener { v ->
                val cb = v as CheckBox
                val contact: SubCategory = cb.tag as SubCategory
                contact.isfall=cb.isChecked
                subCategory.isfall=cb.isChecked
            })
        }


    }

    fun getStudentist(): List<SubCategory?>? {
        return stList
    }


    fun updateRepoList(subCategories: List<SubCategory>) {
        this.subCategories = subCategories
        notifyDataSetChanged()
    }
}

