package com.example.demo.ext

import androidx.fragment.app.Fragment

fun Fragment.addChildFragmentToView(fragment: Fragment, containerId: Int) {
    childFragmentManager.beginTransaction()
        .replace(containerId, fragment)
        .commit()
}