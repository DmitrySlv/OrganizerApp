package com.dscreate_app.organizerapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.dscreate_app.organizerapp.activities.MainApp
import com.dscreate_app.organizerapp.activities.NoteActivity
import com.dscreate_app.organizerapp.databinding.FragmentNotesBinding
import com.dscreate_app.organizerapp.view_models.MainViewModel
import com.dscreate_app.organizerapp.view_models.MainViewModelFactory

class NotesFragment : BaseFragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding: FragmentNotesBinding
        get() = _binding ?: throw RuntimeException("FragmentNotesBinding is null")

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory((context?.applicationContext as MainApp).database)
    }
    private lateinit var editLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onEditResult()
    }

    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NoteActivity::class.java))
    }

    private fun onEditResult() {
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "title: ${it.data?.getStringExtra(TITLE_KEY)}")
                Log.d(TAG, "description: ${it.data?.getStringExtra(DESCRIP_KEY)}")
            }
        }
    }

    companion object {
        const val TAG = "MyLog"
        const val TITLE_KEY = "title_key"
        const val DESCRIP_KEY = "description_key"

        @JvmStatic
        fun newInstance() = NotesFragment()
    }
}