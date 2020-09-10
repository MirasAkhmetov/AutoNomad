package com.autonomad.ui.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.login.City
import com.autonomad.utils.PagingViewHolder
import kotlinx.android.synthetic.main.item_city.view.*

class CitiesAdapter(private val onCityClick: (Int) -> Unit) : RecyclerView.Adapter<CitiesAdapter.CityViewHolder>() {
    var items = emptyList<City>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CityViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = items[position]
        holder.itemView.apply {
            tv_city_name.text = city.name
            setOnClickListener { onCityClick(city.id) }
        }
    }

    class CityViewHolder(view: View) : PagingViewHolder(view)
}