package com.example.noteappktor.ui.notes

import android.content.SharedPreferences
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteappktor.R
import com.example.noteappktor.adapters.NoteAdapter
import com.example.noteappktor.databinding.FragmentNotesBinding
import com.example.noteappktor.other.Constanst.KEY_LOGGED_IN_EMAIL
import com.example.noteappktor.other.Constanst.KEY_PASSWORD
import com.example.noteappktor.other.Constanst.NO_EMAIL
import com.example.noteappktor.other.Constanst.NO_PASSWORD
import com.example.noteappktor.other.Resource
import com.example.noteappktor.other.Status
import com.example.noteappktor.ui.BaseFragment
import com.example.noteappktor.ui.auth.AuthFragmentDirections
import com.skydoves.colorpickerview.kotlin.colorPickerDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class NotesFragment:BaseFragment(R.layout.fragment_notes) {
    private lateinit var binding : FragmentNotesBinding
    private lateinit var noteAdapter: NoteAdapter
    private val viewModel: NotesViewModel by viewModels()
    @Inject
    lateinit var sharedPRef : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentNotesBinding.inflate(inflater,container,false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().requestedOrientation = SCREEN_ORIENTATION_USER
        setUpRecyclerView()
        suscribeToObservers()
        noteAdapter.setOnItemClickListener {
            findNavController().navigate(
                NotesFragmentDirections.actionNotesFragmentToNoteDetailFragment(it.id)
            )
        }

        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(""))
        }
    }

    private fun suscribeToObservers(){
        viewModel.allNotes.observe(viewLifecycleOwner){
            it?.let { event ->
                val result = event.peekContent()
                when(result.status){
                    Status.SUCCESS->{
                        noteAdapter.notes = result.data!!
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    Status.LOADING->{
                        result.data?.let { notes ->
                            noteAdapter.notes = notes
                        }
                        binding.swipeRefreshLayout.isRefreshing = true
                    }
                    Status.ERROR->{
                        event.getContentIfNotHandled()?.let { errorResource ->
                            errorResource.message?.let { message ->
                                showSnackbar(message)
                            }
                        }
                        result.data?.let{ notes ->
                            noteAdapter.notes = notes
                        }
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }
    }


    private fun setUpRecyclerView() = binding.rvNotes.apply {
        noteAdapter = NoteAdapter()
        adapter = noteAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun logout() {
        sharedPRef.edit().putString(KEY_LOGGED_IN_EMAIL, NO_EMAIL).apply()
        sharedPRef.edit().putString(KEY_PASSWORD, NO_PASSWORD).apply()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.notesFragment,true)
            .build()
        findNavController().navigate(
            NotesFragmentDirections.actionNotesFragmentToAuthFragment(),
            navOptions
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu,menu)
    }
}