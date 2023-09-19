package com.dscreate_app.organizerapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscreate_app.organizerapp.activities.MainApp
import com.dscreate_app.organizerapp.adapters.NotesAdapter
import com.dscreate_app.organizerapp.databinding.FragmentShopListNamesBinding
import com.dscreate_app.organizerapp.utils.OrganizerConsts.TAG
import com.dscreate_app.organizerapp.utils.dialogs.NewListDialog
import com.dscreate_app.organizerapp.view_models.MainViewModel
import com.dscreate_app.organizerapp.view_models.MainViewModelFactory

class ShopListNamesFragment : BaseFragment() {

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
        mainViewModel.allNotes.observe(viewLifecycleOwner) {
        }
    }

    override fun onClickNew() {
        NewListDialog.showDialog(requireContext(), object : NewListDialog.Listener {
            override fun onClick(name: String) {
                Log.d(TAG, "Name: $name")
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance() = ShopListNamesFragment()
    }
}