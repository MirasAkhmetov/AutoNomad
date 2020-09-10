package com.autonomad.ui.claims.user_claims.claims.claimtwo

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import kotlinx.android.synthetic.main.item_image_add_closable.view.*

class AddOfferImageAdapter(private val onAddClick: () -> Unit) :
    RecyclerView.Adapter<AddOfferImageAdapter.AddOfferImageViewHolder>() {

    class AddOfferImageViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val newImages = ArrayList<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AddOfferImageViewHolder(
        LayoutInflater.from(parent.context).inflate(
            if (viewType == ITEM_IMAGE) R.layout.item_image_add_closable else R.layout.item_image_add_new,
            parent, false)
    )

    override fun getItemCount() = newImages.size + 1

    override fun onBindViewHolder(holder: AddOfferImageViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_IMAGE) {
            holder.itemView.apply {
                val item = newImages[position - 1]
                iv_image.setImageBitmap(item)

                iv_close.setOnClickListener {
                    newImages.removeAt(position - 1)
                    notifyDataSetChanged()
                }
            }
        } else holder.itemView.setOnClickListener { onAddClick() }
    }

    override fun getItemViewType(position: Int) = if (position != 0) ITEM_IMAGE else ITEM_ADD

    fun addImage(image: Bitmap) {
        newImages.add(image)
        notifyDataSetChanged()
    }

    fun getImages(): List<Bitmap> = newImages

    companion object {
        private const val ITEM_IMAGE = 0
        private const val ITEM_ADD = 1
    }
}