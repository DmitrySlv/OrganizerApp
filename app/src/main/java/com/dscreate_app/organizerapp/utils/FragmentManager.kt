package com.dscreate_app.organizerapp.utils

import androidx.appcompat.app.AppCompatActivity
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.fragments.BaseFragment

object FragmentManager {
    var currentFrag: BaseFragment? = null

    fun setFragment(newFrag: BaseFragment, activity: AppCompatActivity) {
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.container, newFrag)
            .commit()
        currentFrag = newFrag
    }
}