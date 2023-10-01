package com.dscreate_app.organizerapp.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.adapters.ShoppingListItemAdapter
import com.dscreate_app.organizerapp.data.entities.LibraryItemEntity
import com.dscreate_app.organizerapp.data.entities.ShoppingListItemEntity
import com.dscreate_app.organizerapp.data.entities.ShoppingListNameEntity
import com.dscreate_app.organizerapp.databinding.ActivityShoppingListBinding
import com.dscreate_app.organizerapp.utils.OrganizerAppConsts
import com.dscreate_app.organizerapp.utils.OrganizerAppConsts.EMPTY
import com.dscreate_app.organizerapp.utils.ShareHelper
import com.dscreate_app.organizerapp.utils.dialogs.EditListItemDialog
import com.dscreate_app.organizerapp.view_models.MainViewModel
import com.dscreate_app.organizerapp.view_models.MainViewModelFactory

class ShoppingListActivity : AppCompatActivity(), ShoppingListItemAdapter.OnClickListener {
    private val binding by lazy { ActivityShoppingListBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((applicationContext as MainApp).database)
    }
    private var shoppingListName: ShoppingListNameEntity? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ShoppingListItemAdapter? = null
    private lateinit var textWatcher: TextWatcher
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
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
        textWatcher = textWatcher()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.save_item -> { addNewShoppingItem(edItem?.text.toString()) }
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
                edItem?.addTextChangedListener(textWatcher)
                libraryItemObserver()
                shoppingListName?.id?.let {
                    mainViewModel.getAllShoppingListItems(it).removeObservers(
                        this@ShoppingListActivity)
                }
                mainViewModel.getAllLibraryItems("%%") // Показывает все элементы
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textWatcher)
                invalidateOptionsMenu()
                mainViewModel.libraryItems.removeObservers(this@ShoppingListActivity)
                edItem?.setText(EMPTY)
                listItemObserver()
                return true
            }
        }
    }

    private fun textWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mainViewModel.getAllLibraryItems("%$s%") //показывает результат по 1 введённому символу
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
    }

    private fun init() = with(binding) {
        setRcView()
        title = getString(R.string.shopping_list)
        shoppingListName = intent.parcelable<ShoppingListNameEntity>(
            OrganizerAppConsts.SHOPPING_LIST_NAME) as ShoppingListNameEntity
    }

    private fun setRcView() = with(binding) {
        adapter = ShoppingListItemAdapter(this@ShoppingListActivity)
        rcView.layoutManager = LinearLayoutManager(this@ShoppingListActivity)
        rcView.adapter = adapter
    }

    private inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    private fun addNewShoppingItem(name: String) {
        if (name.isEmpty()) return
        val newItem = ShoppingListItemEntity(
                null,
                name,
                EMPTY,
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
                if (it.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                } else {
                    binding.tvEmpty.visibility =  View.GONE
                }
            }
        }
    }

    private fun libraryItemObserver() {
        mainViewModel.libraryItems.observe(this) {
            val tempShoppingList = mutableListOf<ShoppingListItemEntity>()
            it.forEach { item ->
                val shoppingItem = ShoppingListItemEntity(
                    item.id,
                    item.name,
                    EMPTY,
                    false,
                    LIST_ID,
                    ITEM_TYPE_LIBRARY
                )
                tempShoppingList.add(shoppingItem)
            }
            adapter?.submitList(tempShoppingList)
            if (it.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.tvEmpty.visibility =  View.GONE
            }
        }
    }

    override fun onClickItem(shoppingListItem: ShoppingListItemEntity, state: Int) {
        when (state) {
            OrganizerAppConsts.EDIT -> { editListItem(shoppingListItem) }
            OrganizerAppConsts.CHECK_BOX -> {
                mainViewModel.updateShoppingListItem(shoppingListItem)
            }
            OrganizerAppConsts.EDIT_LIBRARY_ITEM -> { editLibraryItem(shoppingListItem) }
            OrganizerAppConsts.DELETE_LIBRARY_ITEM -> {
                shoppingListItem.id?.let { mainViewModel.deleteLibraryItem(it) }
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%") // вручную обновление списка по символу из edItem.
            }
            OrganizerAppConsts.ADD -> { addNewShoppingItem(shoppingListItem.name) }
        }
    }

    private fun editListItem(shoppingListItem: ShoppingListItemEntity) {
        EditListItemDialog.showDialog(
            this, shoppingListItem, object : EditListItemDialog.Listener {
            override fun onClick(item: ShoppingListItemEntity) {
                mainViewModel.updateShoppingListItem(item)
            }
        })
    }

    private fun editLibraryItem(shoppingListItem: ShoppingListItemEntity) {
        EditListItemDialog.showDialog(
            this, shoppingListItem, object : EditListItemDialog.Listener {
                override fun onClick(item: ShoppingListItemEntity) {
                    mainViewModel.updateLibraryItem(LibraryItemEntity(item.id, item.name))
                    mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%") // вручную обновление списка по символу из edItem.
                }
            })
    }

    override fun onBackPressed() {
        saveItemCount()
        super.onBackPressed()
    }

    private fun saveItemCount() {
        var checkedItemCounter = 0
        adapter?.currentList?.forEach {
            if (it.itemChecked) {
                checkedItemCounter++
            }
        }
        val tempShoppingListNameItem = adapter?.itemCount?.let {
            shoppingListName?.copy(
                allItemsCounter = it,
                checkedItemsCounter = checkedItemCounter
            )
        }
        tempShoppingListNameItem?.let { mainViewModel.updateShoppingListName(it) }
    }

    private fun getSelectedTheme(): Int {
        return if (sharedPref.getString("theme_key", "Blue") == "Blue") {
            R.style.Base_Theme_OrganizerAppBlue
        } else if (sharedPref.getString("theme_key", "Green") == "Green") {
            R.style.Base_Theme_OrganizerAppGreen
        } else {
            R.style.Base_Theme_OrganizerAppYellow
        }
    }

    companion object {
        private const val ITEM_TYPE = 0
        private const val ITEM_TYPE_LIBRARY = 1
        private const val LIST_ID = 0
    }
}