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
import com.dscreate_app.organizerapp.settings_views.SettingsActivity
import com.dscreate_app.organizerapp.utils.FragmentManager

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var currentMenuItemId = R.id.notes
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setContentView(binding.root)
        setBottomNavListener()
        FragmentManager.setFragment(NotesFragment.newInstance(), this)
    }

    override fun onResume() {
        super.onResume()
        binding.bNavView.selectedItemId = currentMenuItemId
    }

    private fun setBottomNavListener() = with(binding) {
        bNavView.selectedItemId = R.id.notes
        bNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                   startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                }

                R.id.notes -> {
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(NotesFragment.newInstance(), this@MainActivity)
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
}