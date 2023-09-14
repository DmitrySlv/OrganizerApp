package com.dscreate_app.organizerapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.databinding.ActivityNoteBinding
import com.dscreate_app.organizerapp.utils.showToast

class NoteActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNoteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        actionBarSettings()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                finish()
            }
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun actionBarSettings() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}