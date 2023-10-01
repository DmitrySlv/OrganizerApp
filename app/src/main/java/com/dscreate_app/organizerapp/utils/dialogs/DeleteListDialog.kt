package com.dscreate_app.organizerapp.utils.dialogs


import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.dscreate_app.organizerapp.databinding.DeleteListDialogBinding
import com.dscreate_app.organizerapp.databinding.NewListDialogBinding

object DeleteListDialog {

    fun showDialog(context: Context, listener: Listener) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = DeleteListDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            bDelete.setOnClickListener {
               listener.onClick()
                dialog?.dismiss()
            }
            bCancel.setOnClickListener {
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick()
    }
}