package com.dscreate_app.organizerapp.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.data.entities.NoteItemEntity
import com.dscreate_app.organizerapp.databinding.ActivityNoteBinding
import com.dscreate_app.organizerapp.utils.OrganizerConsts.DATE_FORMAT
import com.dscreate_app.organizerapp.utils.OrganizerConsts.EDIT_STATE_KEY
import com.dscreate_app.organizerapp.utils.OrganizerConsts.EMPTY
import com.dscreate_app.organizerapp.utils.OrganizerConsts.NEW
import com.dscreate_app.organizerapp.utils.OrganizerConsts.NEW_NOTE_KEY
import com.dscreate_app.organizerapp.utils.OrganizerConsts.UPDATE
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NoteActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNoteBinding.inflate(layoutInflater) }
    private var note: NoteItemEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        actionBarSettings()
        getNote()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                setMainResult()
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

    private fun getNote() {
        val pNote = intent.parcelable<NoteItemEntity>(NEW_NOTE_KEY)
        if (pNote != null) {
            note = intent.parcelable<NoteItemEntity>(NEW_NOTE_KEY) as NoteItemEntity
            fillNote()
        }
    }

    private fun fillNote() = with(binding) {
            edTitle.setText(note?.title)
            edDescription.setText(note?.content)
    }

    private inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    private fun setMainResult() {
        var editState = NEW
        val tempNote: NoteItemEntity? = if (note == null) {
            createNewNote()
        } else {
            editState = UPDATE
            updateNote()
        }
        val intent = Intent().apply {
            putExtra(NEW_NOTE_KEY, tempNote)
            putExtra(EDIT_STATE_KEY, editState)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun updateNote(): NoteItemEntity? = with(binding) {
      return note?.copy(
            title = edTitle.text.toString(),
            content = edDescription.text.toString()
        )
    }

    private fun getCurrentTime(): String {
        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    private fun createNewNote(): NoteItemEntity = with(binding) {
        return NoteItemEntity(
            null,
            edTitle.text.toString(),
            edDescription.text.toString(),
            getCurrentTime(),
            EMPTY
        )
    }
}