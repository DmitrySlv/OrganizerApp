package com.dscreate_app.organizerapp.utils.settings_views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.utils.OrganizerAppConsts
import com.dscreate_app.organizerapp.utils.OrganizerAppConsts.TAG
import com.dscreate_app.organizerapp.utils.billing.BillingManager

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var removeAdsPref: Preference
    private lateinit var billingManager: BillingManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        billingManager.closeConnection()
    }

    private fun init() {
        billingManager = BillingManager(activity as AppCompatActivity)
        removeAdsPref = findPreference("remove_ads_key")!!
        removeAdsPref.setOnPreferenceClickListener {
            billingManager.startConnection()
            true
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}