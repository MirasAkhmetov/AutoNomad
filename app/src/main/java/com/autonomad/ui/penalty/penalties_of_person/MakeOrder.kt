package com.autonomad.ui.penalty.penalties_of_person

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.utils.navigateBack
import kotlinx.android.synthetic.main.fragment_penalty_make_order.*

class MakeOrder : Fragment(R.layout.fragment_penalty_make_order) {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        web_view.apply {
            settings.javaScriptEnabled = true
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            loadUrl(arguments?.getString("url").toString())
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    findNavController().navigate(R.id.action_makeOrder_to_penalty_history)
                    return false
                }
            }
        }
        navigateBack()
    }
}