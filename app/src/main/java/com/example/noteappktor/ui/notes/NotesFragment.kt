package com.example.noteappktor.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.noteappktor.R
import com.example.noteappktor.databinding.FragmentNotesBinding
import com.example.noteappktor.ui.BaseFragment

class NotesFragment:BaseFragment(R.layout.fragment_notes) {
    private lateinit var binding : FragmentNotesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(inflater,container,false)

        return binding.root
    }
}