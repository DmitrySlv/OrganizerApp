package com.dscreate_app.organizerapp.utils.dialogs


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.dscreate_app.organizerapp.data.entities.ShoppingListItemEntity
import com.dscreate_app.organizerapp.databinding.EditShoppingListItemDialogBinding
import com.dscreate_app.organizerapp.utils.OrganizerConsts

object EditListItemDialog {

    fun showDialog(context: Context, item: ShoppingListItemEntity, listener: Listener) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = EditShoppingListItemDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            edName.setText(item.name)
            edInfo.setText(item.itemInfo)
            if (item.itemType == LIBRARY_ITEM_TYPE) {
                edInfo.visibility = View.GONE
            }
            bUpdate.setOnClickListener {
                if (edName.text.toString().isNotEmpty()) {
                    val itemInfo = edInfo.text.toString().ifEmpty { OrganizerConsts.EMPTY }
                    listener.onClick(item.copy(
                        name = edName.text.toString(),
                        itemInfo = itemInfo
                    ))
                }
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick(item: ShoppingListItemEntity)
    }

    private const val LIBRARY_ITEM_TYPE = 1
}