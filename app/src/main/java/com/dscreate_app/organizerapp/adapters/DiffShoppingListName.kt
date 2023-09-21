package com.dscreate_app.organizerapp.adapters

import androidx.recyclerview.widget.DiffUtil
import com.dscreate_app.organizerapp.data.entities.ShoppingListNameEntity

object DiffShoppingListName: DiffUtil.ItemCallback<ShoppingListNameEntity>() {
    override fun areItemsTheSame(
        oldItem: ShoppingListNameEntity,
        newItem: ShoppingListNameEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ShoppingListNameEntity,
        newItem: ShoppingListNameEntity
    ): Boolean {
        return oldItem == newItem
    }
}