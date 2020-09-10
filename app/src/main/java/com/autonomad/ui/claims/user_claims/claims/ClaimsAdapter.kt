package com.autonomad.ui.claims.user_claims.claims

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.R
import com.autonomad.data.models.claim_user.Services
import com.autonomad.databinding.ItemClaimsUserClaimsBinding
import com.autonomad.ui.bottom_sheet.claim.GeneralClaim
import com.autonomad.utils.Methods
import kotlinx.android.synthetic.main.item_penalty_driver.view.properties

class ClaimsAdapter(private val claimsAdapter: ClaimsViewModel, val context: Context) :
    RecyclerView.Adapter<ClaimsAdapter.ClaimsAdapterViewHolder>() {

    var services: List<Services> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimsAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemClaimsUserClaimsBinding.inflate(inflater, parent, false)
        return ClaimsAdapterViewHolder(dataBinding, claimsAdapter, context)
    }

    override fun getItemCount() = services.size

    override fun onBindViewHolder(holder: ClaimsAdapterViewHolder, position: Int) {
        holder.bind(services[position])
    }

    class ClaimsAdapterViewHolder constructor(
        private val dataBinding: ViewDataBinding,
        private val claimHomeViewModel: ClaimsViewModel,
        val context: Context
    ) : RecyclerView.ViewHolder(dataBinding.root) {
//        val properties=view.properties
//        val rectangle=view.rectangle
//        val replies=view.replies
        fun bind(services: Services) {
            dataBinding.setVariable(BR.itemData, services)
            dataBinding.executePendingBindings()
//            story_tag.text = story
//            replies.setOnClickListener {
//                view.findNavController().navigate(R.id.action_Claim_to_replies)
//            }
            val bottomSheet = GeneralClaim(object : GeneralClaim.Chosee {

                override fun delete() {
                    claimHomeViewModel.deleteServicereq(
                        Methods.getToken(),
                        services.id.toString(),
                        services
                    )
                }

                override fun edit() {
                    val bundle = bundleOf("requestId" to services.id)
                    //dataBinding.root.findNavController().navigate(R.id.action_claim_user_claims_to_detailClaim2, bundle)
                    dataBinding.root.findNavController().navigate(R.id.action_claim_usser_feedback_to_detailClaim, bundle)
                }

                override fun cancel() {

                }

            }, context)
            bottomSheet.window?.findViewById<View>(R.id.design_bottom_sheet)
                ?.setBackgroundResource(android.R.color.transparent)
            dataBinding.root.properties.setOnClickListener {
                //dataBinding.root.rectangle.background=ContextCompat.getDrawable(context,R.drawable.background_grey_9_16dp_rectangle)
                bottomSheet.show()
            }
            dataBinding.root.setOnClickListener {
                val bundle = bundleOf("UIN" to services.id.toString())
                dataBinding.root.findNavController().navigate(R.id.action_claim_user_claims_to_detailClaim2, bundle)
            }

        }
    }
}