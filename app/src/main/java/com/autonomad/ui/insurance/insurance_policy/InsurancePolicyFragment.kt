package com.autonomad.ui.insurance.insurance_policy

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.utils.navigateBack
import kotlinx.android.synthetic.main.fragment_insurance_policy.*

class InsurancePolicyFragment : Fragment(R.layout.fragment_insurance_policy) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateBack(R.id.action_insurance_policy_to_insurance_home)

        cv_ogpo.setOnClickListener {
            findNavController().navigate(
                R.id.action_insurance_policy_to_insurance_policy_details,
                bundleOf(Pair("type", 0))
            )
        }
        cv_mst.setOnClickListener {
            findNavController().navigate(
                R.id.action_insurance_policy_to_insurance_policy_details,
                bundleOf(Pair("type", 2))
            )
        }
        cv_kasko.setOnClickListener {
            findNavController().navigate(
                R.id.action_insurance_policy_to_insurance_policy_details,
                bundleOf(Pair("type", 1))
            )
        }
    }

}