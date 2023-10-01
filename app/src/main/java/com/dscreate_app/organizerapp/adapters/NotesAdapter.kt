package com.dscreate_app.organizerapp.adapters

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.data.entities.NoteItemEntity
import com.dscreate_app.organizerapp.databinding.NotesItemBinding
import com.dscreate_app.organizerapp.utils.HtmlManager
import com.dscreate_app.organizerapp.utils.TimeManager

class NotesAdapter(
    private val deleteListener: DeleteListener,
    private val onClickItemListener: OnClickListener,
    private val sharedPref: SharedPreferences
): ListAdapter<NoteItemEntity, NotesAdapter.Holder>(DiffNotesAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notes_item, parent, false)
        return Holder(view, deleteListener, onClickItemListener, sharedPref)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(getItem(position))
    }

    class Holder(
        itemView: View,
        private val deleteListener: DeleteListener,
        private val onClickItemListener: OnClickListener,
        private val sharedPref: SharedPreferences
    ): ViewHolder(itemView) {
        private val binding = NotesItemBinding.bind(itemView)

        fun setData(note: NoteItemEntity) = with(binding) {
            tvTitle.text = note.title
            tvDescription.text = HtmlManager.getFromHtml(note.content).trim()
            tvTime.text = TimeManager.getTimeFormat(note.time, sharedPref)
            onClicksItem(note)
        }

        private fun onClicksItem(note: NoteItemEntity) = with(binding) {
            ibDelete.setOnClickListener {
                note.id?.let { note -> deleteListener.deleteItem(note) }
            }
            itemView.setOnClickListener {
                onClickItemListener.onClickItem(note)
            }
        }
    }

    interface DeleteListener {
        fun deleteItem(id: Int)
    }

    interface OnClickListener {
        fun onClickItem(note: NoteItemEntity)
    }
}