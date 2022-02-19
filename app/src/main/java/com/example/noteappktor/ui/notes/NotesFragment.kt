package com.example.noteappktor.ui.notes

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
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
import com.example.noteappktor.ui.BaseFragment
import com.example.noteappktor.ui.auth.AuthFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class NotesFragment:BaseFragment(R.layout.fragment_notes) {
    private lateinit var binding : FragmentNotesBinding
    private lateinit var noteAdapter: NoteAdapter
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
        setUpRecyclerView()
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