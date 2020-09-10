package com.autonomad.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.autonomad.R
import kotlinx.android.synthetic.main.fragment_profile_settings.*

class NewDocument :Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_new_document,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ic_back.setOnClickListener {
            //findNavController().navigate(R.id.action_global_profile)
            activity?.onBackPressed()
        }
        //navigateBack(R.id.action_global_profile)
    }

}