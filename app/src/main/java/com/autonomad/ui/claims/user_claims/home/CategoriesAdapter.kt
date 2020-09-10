package com.autonomad.ui.claims.user_claims.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.R
import com.autonomad.data.models.claim_user.Category
import com.autonomad.databinding.ItemClaimsUserHomeCategoriesBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_claims_user_home_categories.view.*

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesAdapterViewHolder>() {

    var categories: List<Category> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemClaimsUserHomeCategoriesBinding.inflate(inflater, parent, false)
        return CategoriesAdapterViewHolder(dataBinding)
    }


    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: CategoriesAdapterViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    class CategoriesAdapterViewHolder(private val dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(category: Category) {
            dataBinding.setVariable(BR.itemData, category)
            dataBinding.executePendingBindings()

            Picasso.get()
                .load(category.image)
                .placeholder(R.drawable.background_grey_9_16dp_rectangle)
                .into(dataBinding.root.iv_icon)

            dataBinding.root.card_categ.setOnClickListener {
                val bundle = bundleOf("idd" to category.id.toString())
                it.findNavController().navigate(R.id.action_claim_user_home_to_categorieslist, bundle)
            }
        }
    }

    fun updateRepoList(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }
}


