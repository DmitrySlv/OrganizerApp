package com.dscreate_app.organizerapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.dscreate_app.organizerapp.activities.MainApp
import com.dscreate_app.organizerapp.data.entities.ShoppingListNameEntity
import com.dscreate_app.organizerapp.databinding.FragmentShopListNamesBinding
import com.dscreate_app.organizerapp.utils.TimeManager
import com.dscreate_app.organizerapp.utils.dialogs.NewListDialog
import com.dscreate_app.organizerapp.view_models.MainViewModel
import com.dscreate_app.organizerapp.view_models.MainViewModelFactory

class ShoppingListNamesFragment : BaseFragment() {

    private var _binding: FragmentShopListNamesBinding? = null
    private val binding: FragmentShopListNamesBinding
        get() = _binding ?: throw RuntimeException("FragmentShopListNamesBinding is null")

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopListNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun init() = with(binding) {
    }

    private fun observer() {
        mainViewModel.allShoppingListNames.observe(viewLifecycleOwner) {

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
        })
    }

    companion object {
        private const val ALL_ITEMS_COUNTER = 0
        private const val CHECKED_ITEMS_COUNTER = 0
        private const val ITEMS_IDS = 0

        @JvmStatic
        fun newInstance() = ShoppingListNamesFragment()
    }
}