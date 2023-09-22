package com.dscreate_app.organizerapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.data.entities.ShoppingListItemEntity
import com.dscreate_app.organizerapp.data.entities.ShoppingListNameEntity
import com.dscreate_app.organizerapp.databinding.ShoppingListItemBinding
import com.dscreate_app.organizerapp.databinding.ShoppingListNameItemBinding

class ShoppingListItemAdapter(
    private val itemClickListener: OnClickListener,
    private val deleteListener: DeleteListener,
    private val editListener: EditListener
): ListAdapter<ShoppingListItemEntity, ShoppingListItemAdapter.Holder>(DiffShoppingListNameItem) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return if (viewType == SHOPPING_LIST_NAME_ITEM) {
            val shoppingItem = LayoutInflater.from(parent.context)
                .inflate(R.layout.shopping_list_item, parent, false)
            Holder(shoppingItem, itemClickListener, deleteListener, editListener)

        } else {
            val libraryItem = LayoutInflater.from(parent.context)
                .inflate(R.layout.library_list_item, parent, false)
            Holder(libraryItem, itemClickListener, deleteListener, editListener)
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (getItem(position).itemType == SHOPPING_LIST_NAME_ITEM) {
            holder.setItemData(getItem(position))
        } else {
            holder.setLibraryData(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    class Holder(
        itemView: View,
        private val itemClickListener: OnClickListener,
        private val deleteListener: DeleteListener,
        private val editListener: EditListener
    ) : ViewHolder(itemView) {

        fun setItemData(shoppingListItem: ShoppingListItemEntity) {
            val binding = ShoppingListItemBinding.bind(itemView)
            binding.apply {
                tvName.text = shoppingListItem.name
                tvInfo.text = shoppingListItem.itemInfo
                tvInfo.visibility = infoVisibility(shoppingListItem)
            }
            onClicksItemData(shoppingListItem)
        }

        private fun onClicksItemData(shoppingListItem: ShoppingListItemEntity) {
            onClicksLibraryData(shoppingListItem)
        }

        fun setLibraryData(shoppingListItem: ShoppingListItemEntity) {
        }

        private fun onClicksLibraryData(shoppingListItem: ShoppingListItemEntity) {
        }

        private fun infoVisibility(shoppingListItem: ShoppingListItemEntity): Int {
            return if (shoppingListItem.itemInfo.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    interface DeleteListener {
        fun deleteItem(id: Int)
    }

    interface OnClickListener {
        fun onClickItem(shoppingListName: ShoppingListNameEntity)
    }

    interface EditListener {
        fun editItem(shoppingListName: ShoppingListNameEntity)
    }

    companion object {
        private const val SHOPPING_LIST_NAME_ITEM = 0
    }
}