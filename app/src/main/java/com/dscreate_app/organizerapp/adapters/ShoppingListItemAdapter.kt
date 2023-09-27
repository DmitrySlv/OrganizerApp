package com.dscreate_app.organizerapp.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.data.entities.ShoppingListItemEntity
import com.dscreate_app.organizerapp.databinding.LibraryListItemBinding
import com.dscreate_app.organizerapp.databinding.ShoppingListItemBinding
import com.dscreate_app.organizerapp.utils.OrganizerAppConsts.ADD
import com.dscreate_app.organizerapp.utils.OrganizerAppConsts.CHECK_BOX
import com.dscreate_app.organizerapp.utils.OrganizerAppConsts.DELETE_LIBRARY_ITEM
import com.dscreate_app.organizerapp.utils.OrganizerAppConsts.EDIT
import com.dscreate_app.organizerapp.utils.OrganizerAppConsts.EDIT_LIBRARY_ITEM

class ShoppingListItemAdapter(
    private val itemClickListener: OnClickListener
): ListAdapter<ShoppingListItemEntity, ShoppingListItemAdapter.Holder>(DiffShoppingListNameItem) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return if (viewType == SHOPPING_LIST_NAME_ITEM) {
            val shoppingItem = LayoutInflater.from(parent.context)
                .inflate(R.layout.shopping_list_item, parent, false)
            Holder(shoppingItem, itemClickListener)

        } else {
            val libraryItem = LayoutInflater.from(parent.context)
                .inflate(R.layout.library_list_item, parent, false)
            Holder(libraryItem, itemClickListener)
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

    class Holder(itemView: View, private val itemClickListener: OnClickListener): ViewHolder(itemView) {

        fun setItemData(shoppingListItem: ShoppingListItemEntity) {
            val binding = ShoppingListItemBinding.bind(itemView)
            binding.apply {
                tvName.text = shoppingListItem.name
                tvInfo.text = shoppingListItem.itemInfo
                tvInfo.visibility = infoVisibility(shoppingListItem)

                checkBox.isChecked = shoppingListItem.itemChecked
                setPaintFlagAndColor(binding)
            }
            onClicksItemData(shoppingListItem, binding)
        }

        private fun onClicksItemData(
            shoppingListItem: ShoppingListItemEntity, binding: ShoppingListItemBinding
        ) {
           binding.apply {
                checkBox.setOnClickListener {
                    itemClickListener.onClickItem(shoppingListItem.copy(
                        itemChecked = checkBox.isChecked), CHECK_BOX
                    )
                }
                ibEdit.setOnClickListener {
                    itemClickListener.onClickItem(shoppingListItem, EDIT)
                }
            }
        }

        fun setLibraryData(shoppingListItem: ShoppingListItemEntity) {
          val binding=  LibraryListItemBinding.bind(itemView).apply {
                tvName.text = shoppingListItem.name
            }
            onClicksLibraryData(shoppingListItem, binding)
        }

        private fun onClicksLibraryData(
            shoppingListItem: ShoppingListItemEntity, binding: LibraryListItemBinding
        ) {
            binding.apply {
                ibEdit.setOnClickListener {
                    itemClickListener.onClickItem(shoppingListItem, EDIT_LIBRARY_ITEM)
                }
                ibDelete.setOnClickListener {
                    itemClickListener.onClickItem(shoppingListItem, DELETE_LIBRARY_ITEM)
                }
                itemView.setOnClickListener {
                    itemClickListener.onClickItem(shoppingListItem, ADD)
                }
            }
        }

        private fun infoVisibility(shoppingListItem: ShoppingListItemEntity): Int {
            return if (shoppingListItem.itemInfo.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        //Устанавливает цвет и перечеркивание тексту элементу в списке
        private fun setPaintFlagAndColor(binding: ShoppingListItemBinding) {
            binding.apply {
                if (checkBox.isChecked) {
                    tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvInfo.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvName.setTextColor(ContextCompat.getColor(root.context, R.color.gray_light))
                    tvInfo.setTextColor(ContextCompat.getColor(root.context, R.color.gray_light))
                } else {
                    tvName.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvInfo.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvName.setTextColor(ContextCompat.getColor(root.context, R.color.black))
                    tvInfo.setTextColor(ContextCompat.getColor(root.context, R.color.black))
                }
            }
        }
    }

    interface OnClickListener {
        fun onClickItem(shoppingListItem: ShoppingListItemEntity, state: Int)
    }

    companion object {
        const val SHOPPING_LIST_NAME_ITEM = 0
    }
}