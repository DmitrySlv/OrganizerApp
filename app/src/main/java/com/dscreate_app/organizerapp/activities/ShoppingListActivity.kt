package com.dscreate_app.organizerapp.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.adapters.ShoppingListItemAdapter
import com.dscreate_app.organizerapp.data.entities.ShoppingListItemEntity
import com.dscreate_app.organizerapp.data.entities.ShoppingListNameEntity
import com.dscreate_app.organizerapp.databinding.ActivityShoppingListBinding
import com.dscreate_app.organizerapp.utils.OrganizerConsts
import com.dscreate_app.organizerapp.view_models.MainViewModel
import com.dscreate_app.organizerapp.view_models.MainViewModelFactory

class ShoppingListActivity : AppCompatActivity(),
    ShoppingListItemAdapter.OnClickListener,
    ShoppingListItemAdapter.DeleteListener,
    ShoppingListItemAdapter.EditListener
{
    private val binding by lazy { ActivityShoppingListBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((applicationContext as MainApp).database)
    }
    private var shoppingListName: ShoppingListNameEntity? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ShoppingListItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        listItemObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shopping_list_menu, menu)
        saveItem = menu?.findItem(R.id.save_item)!!
        val newItem = menu.findItem(R.id.new_item)
        edItem = newItem.actionView?.findViewById(R.id.edNewShoppingItem) as EditText
        newItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_item) {
            addNewShoppingItem()
        }
        return super.onOptionsItemSelected(item)
    }

    //слушатель открытия/закрытия кнопки меню для создания
    private fun expandActionView(): OnActionExpandListener {
        return object : OnActionExpandListener {

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                saveItem.isVisible = true
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                saveItem.isVisible = false
                invalidateOptionsMenu()
                return true
            }
        }
    }

    private fun init() = with(binding) {
        setRcView()
        title = getString(R.string.shopping_list)
        shoppingListName = intent.parcelable<ShoppingListNameEntity>(
            OrganizerConsts.SHOPPING_LIST_NAME) as ShoppingListNameEntity
    }

    private fun setRcView() = with(binding) {
        adapter = ShoppingListItemAdapter(
            this@ShoppingListActivity,
            this@ShoppingListActivity,
            this@ShoppingListActivity
        )
        rcView.layoutManager = LinearLayoutManager(this@ShoppingListActivity)
        rcView.adapter = adapter
    }

    private inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    private fun addNewShoppingItem() {
        if (edItem?.text.toString().isEmpty()) return
        val newItem = ShoppingListItemEntity(
                null,
                edItem?.text.toString(),
                null,
                ITEM_CHECKED,
                shoppingListName?.id!!,
                ITEM_TYPE
            )
        edItem?.setText("")
        mainViewModel.insertShoppingListItem(newItem)
    }

    private fun listItemObserver() {
        shoppingListName?.id?.let { id ->
            mainViewModel.getAllShoppingListItems(id).observe(this) {
                adapter?.submitList(it)
            }
        }
    }

    override fun deleteItem(id: Int) {
    }

    override fun onClickItem(shoppingListName: ShoppingListNameEntity) {
    }

    override fun editItem(shoppingListName: ShoppingListNameEntity) {
    }

    companion object {
        private const val ITEM_CHECKED = 0
        private const val ITEM_TYPE = 0
    }
}