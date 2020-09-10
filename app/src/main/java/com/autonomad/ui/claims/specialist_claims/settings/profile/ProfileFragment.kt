package com.autonomad.ui.claims.specialist_claims.settings.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.claims.ServiceOffer
import com.autonomad.databinding.FragmentClaimsSpecialistProfileBinding
import com.autonomad.ui.claims.specialist_claims.settings.EditServiceDialog
import kotlinx.android.synthetic.main.fragment_claims_specialist_profile.*

class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var binding: FragmentClaimsSpecialistProfileBinding

    private val serviceAdapter = ServicesAdapter(::showEditDialog)
    private val imagesAdapter = OfferImageAdapter(::onDeleteImage)
    private val reviewsAdapter = ReviewsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentClaimsSpecialistProfileBinding.inflate(inflater, container, false).apply {
            reviewsCount = 0
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialize()
        setOnClicks()
        setAdapter()
    }

    override fun onResume() {
        viewModel.loadData()
        super.onResume()
    }

    private fun setAdapter() {
        rv_services.adapter = serviceAdapter
        rv_photos.adapter = imagesAdapter
        rv_reviews.adapter = reviewsAdapter
    }

    private fun initialize() {
        viewModel.profile.observe(viewLifecycleOwner) {
            it.onSuccess {
                binding.specialist = this
                serviceAdapter.items = offersList
                imagesAdapter.images = images
                rv_photos.isVisible = imagesAdapter.images.isNotEmpty()
            }
            it.onFailure {
                Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
                Log.d("ProfileFragmentLogcat", "initialize: $this")
            }
        }
        viewModel.reviews.observe(viewLifecycleOwner) {
            it.onSuccess {
                reviewsAdapter.reviews = list
                binding.reviewsCount = count
            }
            it.onFailure { Toast.makeText(context, this, Toast.LENGTH_SHORT).show() }
        }
    }

    private fun setOnClicks() {
        iv_back.setOnClickListener { activity?.onBackPressed() }
        layout_profile_info.setOnClickListener { }
        tv_add_service.setOnClickListener {
            findNavController().navigate(R.id.action_specialist_profile_to_specialist_add_offer)
        }
        tv_add_photo.setOnClickListener {
            findNavController().navigate(R.id.action_specialist_profile_to_add_offer_image)
        }
        tv_watch_all_reviews.setOnClickListener { }
    }

    private fun showEditDialog(service: ServiceOffer) {
        val dialog = EditServiceDialog(service, {
            viewModel.update(service.copy(price = it.toIntOrNull() ?: 0))
        }, {
            AlertDialog.Builder(requireContext()).setTitle("Подтвердите действие")
                .setMessage("Вы уверены, что хотите удалить эту услугу?")
                .setNegativeButton("Нет", null)
                .setPositiveButton("Да") { _, _ ->
                    viewModel.delete(service)
                }.show()
        })
        dialog.show(childFragmentManager, "AddService")
    }

    private fun onDeleteImage(id: Int) {
        AlertDialog.Builder(requireContext()).setTitle("Подтвердите действие")
            .setMessage("Вы уверены, что хотите удалить эту услугу?")
            .setNegativeButton("Нет", null)
            .setPositiveButton("Да") { _, _ ->
                viewModel.deleteImage(id)
            }.show()
    }
}