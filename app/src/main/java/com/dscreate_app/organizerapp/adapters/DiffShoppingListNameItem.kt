package com.dscreate_app.organizerapp.adapters

import androidx.recyclerview.widget.DiffUtil
import com.dscreate_app.organizerapp.data.entities.NoteItemEntity
import com.dscreate_app.organizerapp.data.entities.ShoppingListItemEntity
import com.dscreate_app.organizerapp.data.entities.ShoppingListNameEntity

object DiffShoppingListNameItem: DiffUtil.ItemCallback<ShoppingListItemEntity>() {
    override fun areItemsTheSame(
        oldItem: ShoppingListItemEntity,
        newItem: ShoppingListItemEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ShoppingListItemEntity,
        newItem: ShoppingListItemEntity
    ): Boolean {
        return oldItem == newItem
    }
}