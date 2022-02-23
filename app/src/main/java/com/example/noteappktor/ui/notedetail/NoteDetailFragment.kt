package com.example.noteappktor.ui.notedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteappktor.R
import com.example.noteappktor.databinding.FragmentNoteDetailBinding
import com.example.noteappktor.ui.BaseFragment

class NoteDetailFragment:BaseFragment(R.layout.fragment_note_detail) {
    private lateinit var binding: FragmentNoteDetailBinding
    private val args: NoteDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabEditNote.setOnClickListener {
            findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToAddEditNoteFragment(args.id))
        }
    }



}