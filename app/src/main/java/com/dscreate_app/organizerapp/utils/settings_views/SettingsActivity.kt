package com.dscreate_app.organizerapp.utils.settings_views

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.preference.PreferenceManager
import com.dscreate_app.organizerapp.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            startSettingsFrag()
        }
        init()
    }

    private fun startSettingsFrag() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.placeHolder, SettingsFragment.newInstance())
            .commit()
    }

    private fun init() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = getString(R.string.action_bar_title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
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

}