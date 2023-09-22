package com.dscreate_app.organizerapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.activities.MainApp
import com.dscreate_app.organizerapp.activities.ShoppingListActivity
import com.dscreate_app.organizerapp.adapters.ShoppingListNameAdapter
import com.dscreate_app.organizerapp.data.entities.ShoppingListNameEntity
import com.dscreate_app.organizerapp.databinding.FragmentShopListNamesBinding
import com.dscreate_app.organizerapp.utils.OrganizerConsts
import com.dscreate_app.organizerapp.utils.TimeManager
import com.dscreate_app.organizerapp.utils.dialogs.DeleteListDialog
import com.dscreate_app.organizerapp.utils.dialogs.NewListDialog
import com.dscreate_app.organizerapp.view_models.MainViewModel
import com.dscreate_app.organizerapp.view_models.MainViewModelFactory

class ShoppingListNameFragment : BaseFragment(),
    ShoppingListNameAdapter.OnClickListener,
    ShoppingListNameAdapter.DeleteListener,
    ShoppingListNameAdapter.EditListener
{

    private var _binding: FragmentShopListNamesBinding? = null
    private val binding: FragmentShopListNamesBinding
        get() = _binding ?: throw RuntimeException("FragmentShopListNamesBinding is null")

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    private lateinit var adapter: ShoppingListNameAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopListNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun init() = with(binding) {
        activity?.title = getString(R.string.shopping_list_name_title)
        rcView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ShoppingListNameAdapter(
            this@ShoppingListNameFragment,
            this@ShoppingListNameFragment,
            this@ShoppingListNameFragment
        )
        rcView.adapter = adapter
    }

    private fun observer() {
        mainViewModel.allShoppingListNames.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            isViewVisible(it)
        }
    }

    private fun isViewVisible(shoppingListNameEntity: List<ShoppingListNameEntity>) {
        if (shoppingListNameEntity.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            binding.tvEmpty.visibility = View.GONE
        }
    }

    override fun onClickNew() {
        NewListDialog.showDialog(requireContext(), object : NewListDialog.Listener {
            override fun onClick(name: String) {
                val shoppingListName = ShoppingListNameEntity(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    ALL_ITEMS_COUNTER,
                    CHECKED_ITEMS_COUNTER,
                    ITEMS_IDS
                )
                mainViewModel.insertShoppingListName(shoppingListName)
            }
        }, OrganizerConsts.EMPTY)
    }

    override fun deleteItem(id: Int) {
        DeleteListDialog.showDialog(requireContext(), object : DeleteListDialog.Listener {
            override fun onClick() {
                mainViewModel.deleteShoppingListNames(id)
            }
        })
    }

    override fun editItem(shoppingListName: ShoppingListNameEntity) {
        NewListDialog.showDialog(requireContext(), object : NewListDialog.Listener {
            override fun onClick(name: String) {
                mainViewModel.updateShoppingListName(shoppingListName.copy(name = name))
            }
        }, shoppingListName.name)
    }

    override fun onClickItem(shoppingListName: ShoppingListNameEntity) {
        val intent = Intent(requireActivity(), ShoppingListActivity::class.java).apply {
            putExtra(OrganizerConsts.SHOPPING_LIST_NAME, shoppingListName)
        }
        startActivity(intent)
    }

    companion object {
        private const val ALL_ITEMS_COUNTER = 0
        private const val CHECKED_ITEMS_COUNTER = 0
        private const val ITEMS_IDS = 0

        @JvmStatic
        fun newInstance() = ShoppingListNameFragment()
    }
}