package com.autonomad.ui.shop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.ui.shop.model.Product
import kotlinx.android.synthetic.main.shop_product_item_grid.view.*

class ProductGridListAdapter(
    private val context: Context,
    private val list: List<Product>,
    private val onClick: (item: Product) -> Unit,
    private val addToFavourite: (item: Product) -> Unit,
    private val removeFromFavourite: (item: Product) -> Unit
) : RecyclerView.Adapter<ProductGridViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductGridViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.shop_product_item_grid, parent, false)
        return ProductGridViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductGridViewHolder, position: Int) {
        holder.itemView.title.text = "${list[position].article} ${list[position].title}"
        holder.itemView.price.text = "${list[position].price} тг"
        holder.itemView.cont.setOnClickListener {
            //onClick.invoke(list[position])
        }
        //todo image set picasso
    }
}

class ProductGridViewHolder(view: View) : RecyclerView.ViewHolder(view)