package com.dscreate_app.organizerapp.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Spannable
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.data.entities.NoteItemEntity
import com.dscreate_app.organizerapp.databinding.ActivityNoteBinding
import com.dscreate_app.organizerapp.utils.HtmlManager
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
            R.id.bold -> {
                setBoldText()
            }
            R.id.color -> {
                if (!binding.colorPicker.isShown) {
                    openColorPicker()
                } else {
                    closeColorPicker()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun actionBarSettings() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setBoldText() = with(binding) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd
        val styles= edDescription.text.getSpans(startPos, endPos, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if (styles.isNotEmpty()) {
            edDescription.text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }
        edDescription.text.setSpan(boldStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        edDescription.text.trim()
        edDescription.setSelection(startPos)
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
            edDescription.setText(note?.content?.let { HtmlManager.getFromHtml(it).trim() })
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
            content = HtmlManager.toHtml(edDescription.text)
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
           HtmlManager.toHtml(edDescription.text),
            getCurrentTime(),
            EMPTY
        )
    }

    private fun openColorPicker() = with(binding) {
        colorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this@NoteActivity, R.anim.open_color_picker)
        binding.colorPicker.startAnimation(openAnim)
    }

    private fun closeColorPicker() = with(binding) {
        val closeAnim = AnimationUtils.loadAnimation(this@NoteActivity, R.anim.close_color_picker)
        closeAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        binding.colorPicker.startAnimation(closeAnim)
    }
}