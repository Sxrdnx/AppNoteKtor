package com.example.noteappktor.ui.auth

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Bundle
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.example.noteappktor.R
import com.example.noteappktor.databinding.FragmentAuthBinding
import com.example.noteappktor.other.Status
import com.example.noteappktor.ui.BaseFragment
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthFragment:BaseFragment(R.layout.fragment_auth) {
    private lateinit var binding: FragmentAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater,container,false)
        requireActivity().requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
        suscribeToObservers()
        registerUser()
        return binding.root
    }

    private fun registerUser() {
        binding.btnRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString()
            val password = binding.etRegisterPassword.text.toString()
            val confirmedPassword = binding.etRegisterPasswordConfirm.text.toString()
            viewModel.register(email,password,confirmedPassword)

        }
    }

    private fun suscribeToObservers(){
        viewModel.registerStatus.observe(viewLifecycleOwner){ result ->
            when(result.status){
                Status.SUCCESS->{
                    binding.registerProgressBar.visibility = View.GONE
                    showSnackbar(result.data ?: "Cuenta registrada correctmente")
                }
                Status.ERROR->{
                    binding.registerProgressBar.visibility = View.GONE
                    showSnackbar(result.message ?: "Un error ocurrio")
                }
                Status.LOADING->{
                    binding.registerProgressBar.visibility = View.VISIBLE
                }
            }

        }
    }



}