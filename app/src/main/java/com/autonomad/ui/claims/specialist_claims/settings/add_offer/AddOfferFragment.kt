package com.autonomad.ui.claims.specialist_claims.settings.add_offer

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.data.models.claims.ServiceOffer
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_claims_specialist_add_offer.*

class AddOfferFragment : Fragment(R.layout.fragment_claims_specialist_add_offer) {

    private val viewModel by viewModels<AddOfferViewModel>()

    private var offers: List<ServiceOffer> = emptyList()
    private val adapter by lazy {
        OfferAdapter(rg_service) {
            btn_save.isVisible = it.isNotEmpty()
            offers = it
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            rb_category.isChecked = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        iv_back.setOnClickListener { activity?.onBackPressed() }
        rv_services.adapter = adapter
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        viewModel.categories.observe(viewLifecycleOwner) {
            it.onSuccess { adapter.items = this }
            it.onFail(::tt)
            progress_bar.isVisible = it.isLoading && btn_save.isVisible
            btn_save.isEnabled = !it.isLoading
            btn_save.text = if (it.isLoading) "" else getString(R.string.save)
            if (it.isIdle) {
                toast("Услуга успешно добавлена")
                onBackPressedCallback.isEnabled = false
                activity?.onBackPressed()
            }
        }

        rg_service.setOnCheckedChangeListener { _, checkedId ->
            onBackPressedCallback.isEnabled = checkedId == R.id.rb_subcategory
            adapter.notifyDataSetChanged()
        }

        btn_save.setOnClickListener { viewModel.save(offers) }
    }
}