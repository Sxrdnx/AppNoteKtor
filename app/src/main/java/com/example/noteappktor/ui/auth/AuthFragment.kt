package com.example.noteappktor.ui.auth

import android.os.Bundle
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.noteappktor.R
import com.example.noteappktor.databinding.FragmentAuthBinding
import com.example.noteappktor.ui.BaseFragment


class AuthFragment:BaseFragment(R.layout.fragment_auth) {
    private lateinit var binding: FragmentAuthBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToNotesFragment())
        }
    }

}