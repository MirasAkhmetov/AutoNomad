package com.autonomad.ui.insurance.insurance_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.insurance.InsuranceHistory
import com.autonomad.ui.insurance.insurance_home.InsurersAdapter
import kotlinx.android.synthetic.main.fragment_insurance_list.*
import java.io.Serializable


class InsuranceListFragment() : Fragment(R.layout.fragment_insurance_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initialise()
        observe()
//        onClickListener()
//        setImageResourceupAdapter()
    }

    @Suppress("UNCHECKED_CAST")
    private fun observe() {
        arguments?.let { args ->
            Log.d("madiyar", "arguments drivers")
            val drivers = args.getSerializable("drivers") as SerializableLiveData<List<InsuranceHistory>>

            drivers.observe(viewLifecycleOwner, Observer {
                val adapter = InsurersAdapter(
                    it.reversed(),
                    requireContext(),
                    ::showMore
                )
                list.adapter = adapter
            })
        }
    }

    private fun showMore(id: Int) {
        findNavController().navigate(
            R.id.action_insurance_home_to_insurance_page,
            bundleOf("id" to id)
        )
    }

    companion object {
        fun newInstance(
            liveData: SerializableLiveData<List<InsuranceHistory>>
        ): Fragment {
            val args = Bundle()
            args.putSerializable("drivers", liveData as Serializable)

            val fragment = InsuranceListFragment()
            fragment.arguments = args
            return fragment
        }
    }

}