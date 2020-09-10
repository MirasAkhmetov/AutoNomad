package com.autonomad.ui.shop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.ui.shop.model.Product
import kotlinx.android.synthetic.main.shop_product_item_linear.view.*

class ProductLinearListAdapter(
    private val context: Context,
    private val list: List<Product>,
    private val onClick: (item: Product) -> Unit,
    private val addToFavourite: (item: Product) -> Unit,
    private val removeFromFavourite: (item: Product) -> Unit
) : RecyclerView.Adapter<ProductLinearViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductLinearViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.shop_product_item_linear, parent, false)
        return ProductLinearViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductLinearViewHolder, position: Int) {
        holder.itemView.title.text = list[position].title
        holder.itemView.cont.setOnClickListener {
            onClick.invoke(list[position])
        }
        //todo image set picasso
    }
}

class ProductLinearViewHolder(view: View) : RecyclerView.ViewHolder(view)