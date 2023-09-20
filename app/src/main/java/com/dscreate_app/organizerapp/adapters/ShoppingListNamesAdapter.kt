package com.dscreate_app.organizerapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.data.entities.ShoppingListNameEntity
import com.dscreate_app.organizerapp.databinding.ShoppingListNameItemBinding

class ShoppingListNamesAdapter(
    private val itemClickListener: OnClickListener,
    private val deleteListener: DeleteListener,
    private val editListener: EditListener
): ListAdapter<ShoppingListNameEntity, ShoppingListNamesAdapter.Holder>(DiffShoppingListNames) {

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
            tvListName.text = shoppingListNames.name
            tvTime.text = shoppingListNames.time
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