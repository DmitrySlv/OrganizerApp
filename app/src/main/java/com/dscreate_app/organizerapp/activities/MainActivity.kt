package com.dscreate_app.organizerapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.databinding.ActivityMainBinding
import com.dscreate_app.organizerapp.fragments.NotesFragment
import com.dscreate_app.organizerapp.fragments.ShoppingListNameFragment
import com.dscreate_app.organizerapp.settings_views.SettingsActivity
import com.dscreate_app.organizerapp.utils.FragmentManager

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var currentMenuItemId = R.id.notes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}