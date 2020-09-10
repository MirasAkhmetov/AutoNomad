package com.autonomad.ui.claims.specialist_claims.home.repair_auto

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.claims.PluralForms
import com.autonomad.data.models.claims.getPlural
import com.autonomad.ui.claims.specialist_claims.home.RequestsAdapter
import com.autonomad.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.fragment_claims_specialist_repair_auto.*

class RepairAutoFragment : Fragment(R.layout.fragment_claims_specialist_repair_auto), ScrollListener {
    private val subcategoryId by lazy { arguments?.getInt("subcategoryId") ?: 0 }
    private val viewModel by viewModels<RepairAutoViewModel> { RepairAutoViewModelProvider(subcategoryId) }

    private val requestsAdapter = RequestsAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_category.text = arguments?.getString("category").orEmpty()
        ic_sort.setOnClickListener {
            FilterDialog(viewModel.filter, ::onFilterApplied).show(childFragmentManager, "ClaimFilter")
        }
        ic_back.setOnClickListener { requireActivity().onBackPressed() }
        setAdapters()
        observe()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode in listOf(LOCATION_REQUEST_CODE, LOCATION_REQUEST_SETTINGS_CODE)) {
            if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED)
                getLocation()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun loadMore() {
        viewModel.getRequests()
    }

    private fun observe() {
        viewModel.requests.observe(viewLifecycleOwner) {
            it.onSuccess {
                requestsAdapter.items = list
                if (previous == null) rv_claims.scrollToPosition(0)
                if (next == null) requestsAdapter.finish()
                if (subcategoryId != -1)
                    tv_requests_count.text = getPlural(count, PluralForms("заявка", "заявки", "заявок"), false)
            }
            it.onFail(::tt)
        }
    }

    private fun setAdapters() {
        rv_claims.apply {
            adapter = requestsAdapter
            setHasFixedSize(true)
            addOnItemTouchListener { _, position ->
                val args = bundleOf("requestId" to requestsAdapter.items[position].id)
                findNavController().navigate(R.id.action_repairAuto_to_detailClaim, args)
            }
        }
    }

    private fun onFilterApplied(filter: Int) {
        timber("filterApplied: $filter")
        if (filter == FilterDialog.NEAR) checkLocationPermission()
        else viewModel.getRequests(filter)
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            else {
                AlertDialog.Builder(requireContext())
                    .setMessage(getString(R.string.map_turn_on_location_in_settings_title))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri =
                            Uri.fromParts("package", context?.applicationContext?.packageName, null)
                        intent.data = uri
                        startActivityForResult(intent, LOCATION_REQUEST_SETTINGS_CODE)
                    }
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
            }
        } else getLocation()
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            toast(getString(R.string.could_not_get_location_permission))
            return
        }
        val locationProvider = FusedLocationProviderClient(requireActivity())
        locationProvider.lastLocation.addOnCompleteListener {
            val location = it.result
            if (location == null) {
                toast(getString(R.string.location_not_found))
                return@addOnCompleteListener
            }
            viewModel.getRequests(FilterDialog.NEAR, location.latitude.toString(), location.longitude.toString())
        }
    }

    companion object {
        private const val LOCATION_REQUEST_CODE = 465
        private const val LOCATION_REQUEST_SETTINGS_CODE = 466
    }
}