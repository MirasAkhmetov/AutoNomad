package com.autonomad.ui.profile.settings.addresses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.Address
import com.autonomad.data.models.AddressType
import kotlinx.android.synthetic.main.item_settings_address.view.*

class AddressesAdapter(private val listener: OnEditClickListener) :
    RecyclerView.Adapter<AddressesAdapter.AddressViewHolder>() {
    var items: List<Address> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var isEditing = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AddressViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_settings_address, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = items[position]
        holder.itemView.apply {
            iv_edit.isVisible = isEditing
            iv_delete.isVisible = isEditing
            iv_edit.setOnClickListener { listener.onEditClick(address.id) }
            iv_delete.setOnClickListener { listener.onDeleteClick(address.id) }
            iv_address.setImageResource(if (address.type == AddressType.Home.string) R.drawable.ic_bottom_nav_home else R.drawable.ic_address)
            tv_title.text = address.addressType.title ?: address.description
            tv_description.text = address.extra
        }
    }

    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view)
}