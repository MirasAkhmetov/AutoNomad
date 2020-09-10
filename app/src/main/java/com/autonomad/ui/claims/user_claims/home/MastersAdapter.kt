package com.autonomad.ui.claims.user_claims.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.R
import com.autonomad.data.models.claim_user.Master
import com.autonomad.databinding.ItemClaimsUserHomeMastersBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_claims_user_home_masters.view.*

class MastersAdapter : RecyclerView.Adapter<MastersAdapter.MastersAdapterViewHolder>() {

    var masters: List<Master> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MastersAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemClaimsUserHomeMastersBinding.inflate(inflater, parent, false)
        return MastersAdapterViewHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return masters.size
    }

    override fun onBindViewHolder(holder: MastersAdapterViewHolder, position: Int) {
        holder.bind(masters[position])
    }

    class MastersAdapterViewHolder(private val dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(master: Master) {
            dataBinding.setVariable(BR.itemData, master)
            dataBinding.executePendingBindings()

            if (master.prof?.avatar_thumbnail?.isEmpty() == true) {
                dataBinding.root.img_user.setImageResource(R.drawable.ic_profile_user_icon);
            } else {
                Picasso.get()
                    .load(master.prof?.avatar_thumbnail)
                    .placeholder(R.drawable.ic_profile_user_icon)
                    .into(dataBinding.root.img_user)
            }


            dataBinding.root.setOnClickListener {
                val bundle = bundleOf("idd" to master.id.toString())
                it.findNavController().navigate(R.id.action_categories_to_detailMaster, bundle)
            }

        }
    }

    fun updateRepoList(masters: List<Master>) {
        this.masters = masters
        notifyDataSetChanged()
    }

}