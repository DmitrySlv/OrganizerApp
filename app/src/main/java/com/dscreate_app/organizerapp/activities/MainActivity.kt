package com.dscreate_app.organizerapp.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.databinding.ActivityMainBinding
import com.dscreate_app.organizerapp.fragments.NotesFragment
import com.dscreate_app.organizerapp.fragments.ShoppingListNameFragment
import com.dscreate_app.organizerapp.utils.settings_views.SettingsActivity
import com.dscreate_app.organizerapp.utils.FragmentManager
import com.dscreate_app.organizerapp.utils.OrganizerAppConsts
import com.dscreate_app.organizerapp.utils.OrganizerAppConsts.EMPTY
import com.dscreate_app.organizerapp.utils.billing.BillingManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var currentMenuItemId = R.id.notes
    private var currentTheme = EMPTY
    private lateinit var sharedPref: SharedPreferences
    private var interstitialAd: InterstitialAd? = null
    private var adShowCounter = 0
    private val adShowCounterMax = 3
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = sharedPref.getString("theme_key", getString(R.string.def_theme)).toString()
        setTheme(getSelectedTheme())
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(OrganizerAppConsts.MAIN_PREF, MODE_PRIVATE)
        setContentView(binding.root)
        setBottomNavListener()
        FragmentManager.setFragment(NotesFragment.newInstance(), this)
        if (!pref.getBoolean(OrganizerAppConsts.REMOVE_ADS_KEY, false)) {
            loadInterAd()
        }
    }

    override fun onResume() {
        super.onResume()
        if (sharedPref.getString("theme_key", getString(R.string.def_theme)) != currentTheme) {
            recreate()
        }
        binding.bNavView.selectedItemId = currentMenuItemId
    }

    private fun loadInterAd() {
        val request = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            getString(R.string.inter_ad_id),
            request,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(ad: LoadAdError) {
                    interstitialAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
            })
    }

    private fun showInterAd(adListener: AdListener) {
        if (interstitialAd != null || adShowCounter > adShowCounterMax) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterAd()
                    adListener.onFinish()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    interstitialAd = null
                    loadInterAd()
                    adListener.onFinish()
                }

                override fun onAdShowedFullScreenContent() {
                    interstitialAd = null
                    loadInterAd()
                    adListener.onFinish()
                }
            }
            adShowCounter = 0
            interstitialAd?.show(this)
        } else {
            adShowCounter++
            adListener.onFinish()
        }
    }

    private fun setBottomNavListener() = with(binding) {
        bNavView.selectedItemId = R.id.notes
        bNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    showInterAd(object : AdListener {
                        override fun onFinish() {
                            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                        }
                    })
                   startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                }

                R.id.notes -> {
                    showInterAd(object : AdListener {
                        override fun onFinish() {
                            currentMenuItemId = R.id.notes
                            FragmentManager.setFragment(NotesFragment.newInstance(), this@MainActivity)
                        }
                    })
                }

                R.id.shopping_list -> {
                    currentMenuItemId = R.id.shopping_list
                    FragmentManager.setFragment(ShoppingListNameFragment.newInstance(), this@MainActivity)
                }

                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    private fun getSelectedTheme(): Int {
        return if (sharedPref.getString("theme_key", "Blue") == "Blue") {
            R.style.Base_Theme_OrganizerAppBlue
        } else if (sharedPref.getString("theme_key", "Green") == "Green") {
            R.style.Base_Theme_OrganizerAppGreen
        } else {
            R.style.Base_Theme_OrganizerAppYellow
        }
    }

    interface AdListener {
        fun onFinish()
    }
}