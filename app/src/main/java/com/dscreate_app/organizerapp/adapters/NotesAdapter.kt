package com.dscreate_app.organizerapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.data.entities.NoteItemEntity
import com.dscreate_app.organizerapp.databinding.NotesItemBinding

class NotesAdapter: ListAdapter<NoteItemEntity, NotesAdapter.Holder>(DiffNotesAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notes_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(getItem(position))
    }

    class Holder(itemView: View): ViewHolder(itemView) {
        private val binding = NotesItemBinding.bind(itemView)

        fun setData(note: NoteItemEntity) = with(binding) {
            tvTitle.text = note.title
            tvDescription.text = note.content
            tvTime.text = note.time
        }
    }
}