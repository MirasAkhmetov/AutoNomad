package com.autonomad.ui.login

import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.pref.repo.LocalStorage
import com.autonomad.ui.MainActivity
import com.autonomad.ui.login.pin_code_page.PinCodePage
import com.autonomad.utils.Methods
import com.autonomad.utils.PinUtil
import org.koin.android.ext.android.inject

abstract class AuthorizationFragment : Fragment {

    private val localStorage: LocalStorage by inject()

    constructor() : super()

    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    protected fun onAuthorize(token: String) { // todo if token != Methods.key
//        if (localStorage.isPinLoginEnabled()) {
//        if (Methods.key.isEmpty())
        Methods.key = token
        Methods.deleteToken(viewLifecycleOwner) {
        }
        findNavController().navigate(
            R.id.action_global_pinCodePage,
            bundleOf(PinCodePage.FLAG to PinCodePage.FLAG_SET_PIN, MainActivity.MESSAGE to MainActivity.SET_MAIN)
        )
//        } else findNavController().navigate(R.id.action_global_main_page, bundleOf(MainActivity.SET_MAIN to true))
    }
}