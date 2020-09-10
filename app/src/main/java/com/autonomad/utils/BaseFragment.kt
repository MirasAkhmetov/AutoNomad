package com.autonomad.utils

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(), com.autonomad.utils.Fragment {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.requestApplyInsets(view)
        setAdapter()
        setObservers()
        setOnClickListener()
        initialise()
    }

    override fun setAdapter() {}
}
