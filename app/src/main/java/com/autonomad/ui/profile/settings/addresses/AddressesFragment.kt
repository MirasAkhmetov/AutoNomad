package com.autonomad.ui.profile.settings.addresses

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.utils.toast
import kotlinx.android.synthetic.main.fragment_profile_settings_addresses.*

class AddressesFragment : Fragment(R.layout.fragment_profile_settings_addresses), OnEditClickListener {

    private val viewModel by viewModels<AddressesViewModel>()
    private val adapter = AddressesAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        iv_back.setOnClickListener { activity?.onBackPressed() }
        rv_addresses.adapter = adapter
        tv_edit.setOnClickListener {
            adapter.isEditing = !adapter.isEditing
            tv_edit.text = getString(if (adapter.isEditing) R.string.complete else R.string.edit)
            tv_new_address.isVisible = !adapter.isEditing
        }

        tv_new_address.setOnClickListener {
            findNavController().navigate(R.id.action_profile_settings_addresses_to_edit)
        }

        viewModel.addresses.observe(viewLifecycleOwner) {
            it.onSuccess { adapter.items = this }
            it.onFailure { toast(this) }
        }
        viewModel.message.observe(viewLifecycleOwner) { toast(it) }
    }

    override fun onEditClick(id: Int) {
        findNavController().navigate(R.id.action_profile_settings_addresses_to_edit, bundleOf("id" to id))
    }

    override fun onDeleteClick(id: Int) {
        AlertDialog.Builder(context).setTitle("Вы действительно хотите удалить адрес?").setNegativeButton("Нет", null)
            .setPositiveButton("Да") { _, _ ->
                viewModel.deleteAddress(id)
            }.show()
    }
}