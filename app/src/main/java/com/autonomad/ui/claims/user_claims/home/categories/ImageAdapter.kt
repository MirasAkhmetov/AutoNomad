package com.autonomad.ui.claims.user_claims.home.categories

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.R
import com.autonomad.data.models.claim_user.ServiceImage
import com.autonomad.databinding.ItemClaimsUserImageBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_claims_user_image.view.*

class ImageAdapter :
    RecyclerView.Adapter<ImageAdapter.ImageAdapterViewHolder>() {
    var serviceimages: List<ServiceImage> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemClaimsUserImageBinding.inflate(inflater, parent, false)
        return ImageAdapterViewHolder(dataBinding)
    }

    override fun getItemCount() = serviceimages.size

    override fun onBindViewHolder(holder: ImageAdapterViewHolder, position: Int) {
        holder.bind(serviceimages[position])
    }

    class ImageAdapterViewHolder(private val dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root) {
//        val name = view.name
        //val avatarImage = itemView.suret


        fun bind(serviceImage: ServiceImage) {
            dataBinding.setVariable(BR.itemData, serviceImage)
            dataBinding.executePendingBindings()
            Log.d("qwertt", serviceImage.image)

            if (serviceImage.image.isEmpty()) {
                dataBinding.root.suret.setImageResource(R.drawable.background_grey_5_16dp_rectangle);
            } else {
                Picasso.get()
                    .load(serviceImage.image)
                    //.transform(transformation)
                    .placeholder(R.drawable.background_grey_5_16dp_rectangle)
                    .into(dataBinding.root.suret)
            }
//
//            Picasso.get().load(serviceimages.image).into(avatarImage);
//            name.text = master
        }
    }
}