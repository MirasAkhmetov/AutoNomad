package com.autonomad.ui.claims.user_claims.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.R
import com.autonomad.data.models.claim_user.MastID
import com.autonomad.data.models.claim_user.Master
import com.autonomad.databinding.ItemClaimsSpisokDetailBinding
import com.autonomad.utils.Methods
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_claims_spisok_detail.view.*

class FavAdapter(private val detailSubViewModel: FavouritesViewModel, val context: Context) :
    RecyclerView.Adapter<FavAdapter.DetailSpisokAdapterViewHolder>() {
    var masters: List<Master> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailSpisokAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemClaimsSpisokDetailBinding.inflate(inflater, parent, false)
        return DetailSpisokAdapterViewHolder(dataBinding, detailSubViewModel, context)
    }

    override fun getItemCount(): Int {
        return masters.size
    }

    override fun onBindViewHolder(holder: DetailSpisokAdapterViewHolder, position: Int) {
        holder.bind(masters[position])
    }

    class DetailSpisokAdapterViewHolder constructor(
        private val dataBinding: ViewDataBinding,
        private val detailSubViewModel: FavouritesViewModel,
        val context: Context
    ) : RecyclerView.ViewHolder(dataBinding.root) {
        private var mast_id = MastID("")

        fun bind(master: Master) {
            dataBinding.setVariable(BR.itemData, master)
            dataBinding.executePendingBindings()

            mast_id = MastID(master.id.toString())


            if (master.prof?.avatar?.isEmpty() == true) {
                dataBinding.root.ic_user.setImageResource(R.drawable.ic_profile_male);
            } else {
                Picasso.get()
                    .load(master.prof?.avatar)
                    .placeholder(R.drawable.ic_profile_male)
                    .into(dataBinding.root.ic_user)
            }

            if (!master.is_fav) {
                dataBinding.root.starimg.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            } else {
                dataBinding.root.starimg.setImageResource(R.drawable.ic_claims_favourites)
            }

            dataBinding.root.starimg.setOnClickListener {
                if (!master.is_fav) {
                    detailSubViewModel.postFavour(
                        Methods.getToken(),
                        master.id.toString(),
                        mast_id
                    )
                    dataBinding.root.starimg.setImageResource(R.drawable.ic_claims_favourites)
                } else {
                    detailSubViewModel.deleteFavour(
                        Methods.getToken(),
                        master.id.toString()
                    )
                    dataBinding.root.starimg.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                }

            }

            dataBinding.root.setOnClickListener {
                val bundle = bundleOf("idd" to master.id.toString())
                it.findNavController().navigate(R.id.action_claim_user_home_to_detailMaster, bundle)
            }

        }
    }

    fun updateRepoList(masters: List<Master>) {
        this.masters = masters
        notifyDataSetChanged()
    }
}