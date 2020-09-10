package com.autonomad.ui.bottom_sheet.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.Address
import com.autonomad.data.models.AddressType
import kotlinx.android.synthetic.main.item_bottom_sheet_address.view.*

class AddressesAdapter : RecyclerView.Adapter<AddressesAdapter.AddressViewHolder>() {
    var items = emptyList<Address>()
        set(value) {
            field = value
            checkedAddressPosition = -1
        }

    var checkedAddressPosition = -1
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AddressViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_bottom_sheet_address, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = items[position]
        holder.itemView.apply {
            tv_title.text = address.addressType.title ?: address.description
            tv_description.text = address.extra
            iv_address.setImageResource(if (address.type == AddressType.Home.string) R.drawable.ic_bottom_nav_home else R.drawable.ic_address)
            rb_address.isChecked = position == checkedAddressPosition
            rb_address.setOnClickListener { checkedAddressPosition = position }
            setOnClickListener { rb_address.callOnClick() }
        }
    }

    val checkedAddress: Address?
        get() = items.getOrNull(checkedAddressPosition)

    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view)
}