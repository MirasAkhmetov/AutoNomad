package com.autonomad.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import kotlinx.android.synthetic.main.fragment_profile_documents.*


class Documents : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_documents, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        newDocument.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_newDocument)
        }
    }
    private fun setAdapter() {
        val documents = ArrayList<String>()
        documents.add("Honey Smile")
        val documentAdapter = DocumentsAdapter(documents)
        documents_rv.layoutManager = LinearLayoutManager(context)
        documents_rv.setHasFixedSize(true)
        documents_rv.adapter = documentAdapter
    }

}