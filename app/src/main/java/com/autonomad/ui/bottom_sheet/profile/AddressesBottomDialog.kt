package com.autonomad.ui.bottom_sheet.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.bottom_sheet_addresses.*

class AddressesBottomDialog(@IdRes private val destinationToNewAddress: Int, private val onAddressSelected: (Int, String, String, String) -> Unit) :
    RoundBottomDialog() {

    private val viewModel by viewModels<AddressesViewModel>()
    private val adapter = AddressesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_addresses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_addresses.adapter = adapter
        iv_back.setOnClickListener { dismiss() }
        tv_new_address.setOnClickListener {
            findNavController().navigate(destinationToNewAddress)
            dismiss()
        }
        btn_next.setOnClickListener {
            val checkedAddress = adapter.checkedAddress
            if (checkedAddress == null) toast("Выберите адрес")
            else {
                onAddressSelected(checkedAddress.id, checkedAddress.description.toString(), checkedAddress.longitude.toString(), checkedAddress.latitude.toString())
                dismiss()
            }
        }

        viewModel.addresses.observe(viewLifecycleOwner) {
            it.onSuccess { adapter.items = this }
            it.onFail { message -> tt("Не удалось загрузить список адресов", message) }
        }
    }
}