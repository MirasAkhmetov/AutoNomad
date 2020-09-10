package com.autonomad.utils

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.autonomad.ui.NavigationViewModel

abstract class NavigationFragment : Fragment {
    constructor() : super()

    constructor(@LayoutRes layout: Int) : super(layout)

    protected val navigationViewModel by activityViewModels<NavigationViewModel>()
}