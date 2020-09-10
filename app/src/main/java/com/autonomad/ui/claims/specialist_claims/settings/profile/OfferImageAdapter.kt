package com.autonomad.ui.claims.specialist_claims.settings.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.claim_user.ServiceImage
import com.autonomad.utils.loadImage
import kotlinx.android.synthetic.main.item_image_closable.view.*

class OfferImageAdapter(private val onDelete: (Int) -> Unit) : RecyclerView.Adapter<OfferImageAdapter.OfferImageViewHolder>() {

    var images: List<ServiceImage> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OfferImageViewHolder(parent)

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: OfferImageViewHolder, position: Int) {
        val item = images[position]
        holder.itemView.apply {
            iv_image.loadImage(item.image)
            iv_close.setOnClickListener { onDelete(item.id) }
        }
    }

    class OfferImageViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image_closable, parent, false))
}