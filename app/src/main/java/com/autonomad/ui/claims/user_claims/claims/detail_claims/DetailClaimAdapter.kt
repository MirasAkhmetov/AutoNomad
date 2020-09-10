package com.autonomad.ui.claims.user_claims.claims.detail_claims

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.R
import com.autonomad.data.models.claim_user.ClaimMasterResult
import com.autonomad.data.models.claim_user.Master
import com.autonomad.databinding.ItemDetailClaimsBinding
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.item_detail_claims.view.*

class DetailClaimAdapter : RecyclerView.Adapter<DetailClaimAdapter.PersonPenaltyViewHolder>() {

    var masters: List<ClaimMasterResult> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonPenaltyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemDetailClaimsBinding.inflate(inflater, parent, false)
        return PersonPenaltyViewHolder(dataBinding)
    }

    override fun getItemCount() = masters.size

    override fun onBindViewHolder(holder: PersonPenaltyViewHolder, position: Int) {
        holder.bind(masters[position].master)
    }

    class PersonPenaltyViewHolder constructor(private val dataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(master: Master) {
            dataBinding.setVariable(BR.itemData, master)
            dataBinding.executePendingBindings()

            itemView.text_review_count.text = "(".plus(master.review_count).plus(" отзывов)")

            if (!master.prof?.avatar.isNullOrEmpty()) {
                val transformation: Transformation = RoundedTransformationBuilder()
                    .borderColor(Color.BLACK)
                    .borderWidthDp(3f)
                    .cornerRadiusDp(30f)
                    .oval(false)
                    .build()

                Picasso.get()
                    .load(master.prof?.avatar)
                    .transform(transformation)
                    .placeholder(R.drawable.ic_profile_user_icon)
                    .error(R.drawable.ic_profile_user_icon)
                    .into(dataBinding.root.ic_user)
            }
        }
    }
}