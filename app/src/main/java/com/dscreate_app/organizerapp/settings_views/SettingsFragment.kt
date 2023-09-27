package com.dscreate_app.organizerapp.settings_views

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.dscreate_app.organizerapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}