package com.autonomad.ui.claims.specialist_claims.home.detail

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.data.models.claims.ServiceRequest
import com.autonomad.ui.claims.specialist_claims.home.ClaimCategoriesAdapter
import com.autonomad.ui.claims.specialist_claims.home.category.ImageAdapter
import com.autonomad.utils.Methods
import com.autonomad.utils.timber
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_claims_specialist_detail_claim.*

class DetailClaimFragment : Fragment(R.layout.fragment_claims_specialist_detail_claim) {

    private val response by lazy { arguments?.getInt("responseId") ?: 0 }
    private val request by lazy { arguments?.getInt("requestId") ?: 0 }
    private val isResponse by lazy { response != 0 }

    private val viewModel by viewModels<DetailViewModel> {
        timber("$response $request")
        DetailViewModelFactory(if (isResponse) response else request, isResponse)
    }

    private val categoriesAdapter = ClaimCategoriesAdapter()
    private val imagesAdapter = ImageAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ic_back.setOnClickListener { requireActivity().onBackPressed() }
        rv_categories.adapter = categoriesAdapter
        rv_photos.adapter = imagesAdapter
        observe()

        if (isResponse || !Methods.isSpecialist()) btn_respond.isVisible = false
        else btn_respond.setOnClickListener {
            viewModel.respond()
            btn_respond.isEnabled = false
        }
    }

    private fun observe() {
        viewModel.loadData()
        viewModel.request.observe(viewLifecycleOwner) {
            if (!isResponse) {
                progress_bar.isVisible = it.isLoading
                container_content.isVisible = !it.isLoading
            }
            it.onSuccess {
                setView(this)
            }
            it.onFail { message ->
                toast(message)
                timber(message)
            }
        }
        viewModel.response.observe(viewLifecycleOwner) {
            if (isResponse) {
                progress_bar.isVisible = it.isLoading
                container_content.isVisible = !it.isLoading
            }
            it.onSuccess {
                setView(request)
                tv_car_info.text = car?.markModel.orEmpty()
            }
            it.onFail(::tt)
        }
        viewModel.responded.observe(viewLifecycleOwner) {
            it.onSuccess {
                toast("Ваш отклик был принят!")
                btn_respond.isVisible = false
            }
            it.onFail { message ->
                timber(message)
                toast("Не удалось оставить отклик")
                btn_respond.isEnabled = true
            }
            btn_respond.isEnabled = it.isLoading
            btn_respond.text = if (it.isLoading) "" else getString(R.string.respond)
            progress_btn.isVisible = it.isLoading
        }
    }

    private fun setView(request: ServiceRequest) {
        tv_client_name.text = request.profile.firstName
        tv_claim.text = request.title
        tv_claim_description.text = request.description
        tv_date.text = request.time
        tv_price.text = if (request.budget != null) getString(R.string.price, request.budget.toString()) else
            getString(R.string.null_price)
        if (request.negotiablePrice) tv_price.append("\t(${getString(R.string.negotiable)})")
        categoriesAdapter.items = request.subcategories.map { item -> item.name }
        imagesAdapter.items = request.images
        if (Methods.isSpecialist()) {
            tv_state.isVisible = request.status.value in 1..2
            tv_state.text = if (request.status.value == 1) "Активна" else "В процессе"
        } else {
            if (request.status.value == 1) {
                tv_state.text = "Активна"
                tv_state.setTextColor(ContextCompat.getColor(requireContext(), R.color.situational_error))
            } else {
                tv_state.text = "Неактивна"
                tv_state.setTextColor(ContextCompat.getColor(requireContext(), R.color.situational_red_error))
            }
        }
        tv_car_info.text = request.car?.title
    }
}