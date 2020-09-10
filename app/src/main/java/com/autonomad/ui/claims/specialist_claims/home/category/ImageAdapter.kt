package com.autonomad.ui.claims.specialist_claims.home.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.claim_user.ServiceImage
import com.autonomad.databinding.ItemClaimsUserImageBinding

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageAdapterViewHolder>() {

    var items = emptyList<ServiceImage>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ImageAdapterViewHolder(ItemClaimsUserImageBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ImageAdapterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ImageAdapterViewHolder(private val dataBinding: ItemClaimsUserImageBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(serviceImage: ServiceImage) {
            dataBinding.setVariable(BR.itemData, serviceImage)
            dataBinding.executePendingBindings()
        }
    }
}