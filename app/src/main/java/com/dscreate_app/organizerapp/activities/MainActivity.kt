package com.dscreate_app.organizerapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.databinding.ActivityMainBinding
import com.dscreate_app.organizerapp.fragments.NotesFragment
import com.dscreate_app.organizerapp.fragments.NotesFragment.Companion.TAG
import com.dscreate_app.organizerapp.utils.FragmentManager

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setBottomNavListener()
    }

    private fun setBottomNavListener() {
        binding.bNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    Log.d(TAG, "Настройки")
                }

                R.id.notes -> {
                    FragmentManager.setFragment(NotesFragment.newInstance(), this)
                }

                R.id.shopping_list -> {
                    Log.d(TAG, "Список покупок")
                }

                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }
}