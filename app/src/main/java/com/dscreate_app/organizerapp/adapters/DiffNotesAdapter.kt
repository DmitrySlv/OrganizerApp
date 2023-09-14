package com.dscreate_app.organizerapp.adapters

import androidx.recyclerview.widget.DiffUtil
import com.dscreate_app.organizerapp.data.entities.NoteItemEntity

object DiffNotesAdapter: DiffUtil.ItemCallback<NoteItemEntity>() {
    override fun areItemsTheSame(oldItem: NoteItemEntity, newItem: NoteItemEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteItemEntity, newItem: NoteItemEntity): Boolean {
        return oldItem == newItem
    }
}