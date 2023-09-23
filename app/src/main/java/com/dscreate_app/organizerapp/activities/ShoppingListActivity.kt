package com.dscreate_app.organizerapp.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
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
import com.dscreate_app.organizerapp.utils.ShareHelper
import com.dscreate_app.organizerapp.utils.dialogs.EditListItemDialog
import com.dscreate_app.organizerapp.view_models.MainViewModel
import com.dscreate_app.organizerapp.view_models.MainViewModelFactory

class ShoppingListActivity : AppCompatActivity(),
    ShoppingListItemAdapter.OnClickListener,
    ShoppingListItemAdapter.EditItemListener
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
        when(item.itemId) {
            R.id.save_item -> { addNewShoppingItem() }
            R.id.delete_list -> {
                shoppingListName?.id?.let { mainViewModel.deleteShoppingList(it, true) }
                finish()
            }
            R.id.clear_list -> {
                shoppingListName?.id?.let { mainViewModel.deleteShoppingList(it, false) }
            }
            R.id.share_list -> {
                startActivity(Intent.createChooser(
                    adapter?.currentList?.let {
                        shoppingListName?.name?.let { name ->
                        ShareHelper.shareShoppingList(it, name)
                    } },
                    getString(R.string.share_with)
                ))
            }
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
                OrganizerConsts.EMPTY,
                false,
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
                isVisibleView(it)
            }
        }
    }

    private fun isVisibleView(shoppingListItemEntity: List<ShoppingListItemEntity>) {
        if (shoppingListItemEntity.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
            } else {
            binding.tvEmpty.visibility =  View.GONE
        }
    }

    override fun onClickItem(shoppingListItem: ShoppingListItemEntity) {
        mainViewModel.updateShoppingListItem(shoppingListItem)
    }

    override fun editItem(shoppingListItem: ShoppingListItemEntity) {
        EditListItemDialog.showDialog(
            this, shoppingListItem, object : EditListItemDialog.Listener {
            override fun onClick(item: ShoppingListItemEntity) {
                mainViewModel.updateShoppingListItem(item)
            }
        })
    }

    companion object {
        private const val ITEM_TYPE = 0
    }
}