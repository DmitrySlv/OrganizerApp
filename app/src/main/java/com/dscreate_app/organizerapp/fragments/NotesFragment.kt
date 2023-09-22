package com.dscreate_app.organizerapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.activities.MainApp
import com.dscreate_app.organizerapp.activities.NotesActivity
import com.dscreate_app.organizerapp.adapters.NotesAdapter
import com.dscreate_app.organizerapp.data.entities.NoteItemEntity
import com.dscreate_app.organizerapp.databinding.FragmentNotesBinding
import com.dscreate_app.organizerapp.utils.OrganizerConsts.EDIT_STATE_KEY
import com.dscreate_app.organizerapp.utils.OrganizerConsts.NEW_NOTE_KEY
import com.dscreate_app.organizerapp.utils.OrganizerConsts.UPDATE
import com.dscreate_app.organizerapp.view_models.MainViewModel
import com.dscreate_app.organizerapp.view_models.MainViewModelFactory

class NotesFragment : BaseFragment(), NotesAdapter.DeleteListener, NotesAdapter.OnClickListener {

    private var _binding: FragmentNotesBinding? = null
    private val binding: FragmentNotesBinding
        get() = _binding ?: throw RuntimeException("FragmentNotesBinding is null")

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory((context?.applicationContext as MainApp).database)
    }
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        onEditResult()
        observer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun init() = with(binding) {
        activity?.title = getString(R.string.notes)
        adapter = NotesAdapter(this@NotesFragment, this@NotesFragment)
        rcView.layoutManager = LinearLayoutManager(requireContext())
        rcView.adapter = adapter
    }

    private fun observer() {
        mainViewModel.allNotes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            isViewVisible(it)
        }
    }

    private fun isViewVisible(noteItemEntity: List<NoteItemEntity>) {
        if (noteItemEntity.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            binding.tvEmpty.visibility = View.GONE
        }
    }

    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NotesActivity::class.java))
    }

    private fun onEditResult() {
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val editState = it.data?.getStringExtra(EDIT_STATE_KEY)
                if (editState == UPDATE) {
                    it.data?.parcelable<NoteItemEntity>(NEW_NOTE_KEY)
                        ?.let { note -> mainViewModel.updateNote(note) }
                } else {
                    it.data?.parcelable<NoteItemEntity>(NEW_NOTE_KEY)
                        ?.let { note -> mainViewModel.insertNote(note) }
                }

            }
        }
    }

    private inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    override fun deleteItem(id: Int) {
        mainViewModel.deleteNote(id)
    }

    override fun onClickItem(note: NoteItemEntity) {
        val intent = Intent(activity, NotesActivity::class.java).apply {
            putExtra(NEW_NOTE_KEY, note)
        }
        editLauncher.launch(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance() = NotesFragment()
    }
}