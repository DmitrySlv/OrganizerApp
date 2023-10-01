package com.dscreate_app.organizerapp.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.data.entities.ShoppingListNameEntity
import com.dscreate_app.organizerapp.databinding.ShoppingListNameItemBinding

class ShoppingListNameAdapter(
    private val itemClickListener: OnClickListener,
    private val deleteListener: DeleteListener,
    private val editListener: EditListener
): ListAdapter<ShoppingListNameEntity, ShoppingListNameAdapter.Holder>(DiffShoppingListName) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_list_name_item, parent, false)
        return Holder(view, itemClickListener, deleteListener, editListener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(getItem(position))
    }

    class Holder(
        itemView: View,
        private val itemClickListener: OnClickListener,
        private val deleteListener: DeleteListener,
        private val editListener: EditListener
    ): ViewHolder(itemView) {
        private val binding = ShoppingListNameItemBinding.bind(itemView)

        fun setData(shoppingListNames: ShoppingListNameEntity) = with(binding) {
            val counterText = "${shoppingListNames.checkedItemsCounter}/${shoppingListNames.allItemsCounter}"
            val colorState = ColorStateList.valueOf(
                getProgressColorState(shoppingListNames, root.context)
            )

            tvListName.text = shoppingListNames.name
            tvTime.text = shoppingListNames.time
            tvCounter.text = counterText
            pBar.max = shoppingListNames.allItemsCounter
            pBar.progress = shoppingListNames.checkedItemsCounter
            pBar.progressTintList = colorState
            counterCard.backgroundTintList = colorState
            onClicksItem(shoppingListNames)
        }

        private fun onClicksItem(shoppingListNames: ShoppingListNameEntity) = with(binding) {
            ibDelete.setOnClickListener {
                shoppingListNames.id?.let { id -> deleteListener.deleteItem(id) }
            }
            ibEdit.setOnClickListener {
                editListener.editItem(shoppingListNames)
            }
            itemView.setOnClickListener {
                itemClickListener.onClickItem(shoppingListNames)
            }
        }

        private fun getProgressColorState(item: ShoppingListNameEntity, context: Context): Int {
            return if (item.checkedItemsCounter == item.allItemsCounter) {
                ContextCompat.getColor(context, R.color.green_main)
            } else {
                ContextCompat.getColor(context, R.color.red)
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
}