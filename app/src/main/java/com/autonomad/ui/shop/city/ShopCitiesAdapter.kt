package com.autonomad.ui.shop.city

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.login.City
import com.autonomad.utils.PagingViewHolder
import kotlinx.android.synthetic.main.item_city.view.*

class ShopCitiesAdapter(
    private val list: List<City>,
    private val onClick: (item: City) -> Unit
) : RecyclerView.Adapter<CityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CityViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_city,
                parent,
                false
            )
        )

    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = list[position]
        holder.itemView.apply {
            tv_city_name.text = city.name
            setOnClickListener { onClick(city) }
        }
    }
}

class CityViewHolder(view: View) : PagingViewHolder(view)