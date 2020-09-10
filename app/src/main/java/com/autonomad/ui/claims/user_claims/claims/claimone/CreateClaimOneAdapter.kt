package com.autonomad.ui.claims.user_claims.claims.claimone

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.R
import com.autonomad.data.models.claim_user.ServiceCreate
import com.autonomad.data.models.claim_user.Category
import com.autonomad.databinding.ItemClaimsCreateOneBinding
import com.autonomad.ui.claims.user_claims.home.ClaimHomeViewModel
import com.google.gson.Gson

class CreateClaimOneAdapter(private val claimHomeViewModel: ClaimHomeViewModel, val context: Context) :
    RecyclerView.Adapter<CreateClaimOneAdapter.CreateClaimOneViewHolder>() {

    var categories: List<Category> = emptyList()

    var drivers: List<String> = emptyList()
    private var selectedPosition1: Int = -1
    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CreateClaimOneViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemClaimsCreateOneBinding.inflate(inflater, parent, false)
        return CreateClaimOneViewHolder(dataBinding, claimHomeViewModel, context)
    }

    override fun getItemCount(): Int {
        return  categories.size
    }

    override fun onBindViewHolder(holder: CreateClaimOneViewHolder, position: Int) {
        holder.bind(categories.get(position))
    }

    class CreateClaimOneViewHolder constructor(
        private val dataBinding: ViewDataBinding,
        private val claimHomeViewModel: ClaimHomeViewModel,
        val context: Context
    ) : RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(category: Category) {
            dataBinding.setVariable(BR.itemData, category)
            dataBinding.executePendingBindings()

            dataBinding.root.setOnClickListener {
                val serviceCreateModel = ServiceCreate().apply { categoryId = category.id }
                val bundle = bundleOf("serviceCreateModel" to Gson().toJson(serviceCreateModel))
                it.findNavController().navigate(R.id.action_claimone_to_twoclaim, bundle)
            }
        }

    }
    fun updateRepoList(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

}