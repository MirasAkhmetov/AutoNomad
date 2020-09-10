package com.autonomad.ui.profile.settings.Document

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import kotlinx.android.synthetic.main.fragment_profile_doc.*

class DocumentSett : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_doc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }

        new_doc.setOnClickListener {
            findNavController().navigate(R.id.documentSett_newdok)
        }




    }
}