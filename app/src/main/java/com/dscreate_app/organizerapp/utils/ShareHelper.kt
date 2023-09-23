package com.dscreate_app.organizerapp.utils

import android.content.Intent
import com.dscreate_app.organizerapp.data.entities.ShoppingListItemEntity

object ShareHelper {

    fun shareShoppingList(shoppingList: List<ShoppingListItemEntity>, listName: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = TYPE
        intent.apply {
            putExtra(Intent.EXTRA_TEXT, makeShareText(shoppingList, listName))
        }
        return intent
    }

    private fun makeShareText(shoppingList: List<ShoppingListItemEntity>, listName: String): String {
        val sBuilder = StringBuilder()
        sBuilder.append("<<$listName>>")
        sBuilder.append("\n")
        var counter = 0
        shoppingList.forEach {
            sBuilder.append("${++counter} - ${it.name} (${it.itemInfo})")
            sBuilder.append("\n")
        }
        return sBuilder.toString()
    }

    private const val TYPE = "text/plane"
}