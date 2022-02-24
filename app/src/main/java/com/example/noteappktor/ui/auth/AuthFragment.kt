package com.example.noteappktor.ui.auth

import android.content.SharedPreferences
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Bundle
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import com.example.noteappktor.R
import com.example.noteappktor.data.remote.BasicAuthInterceptor
import com.example.noteappktor.databinding.FragmentAuthBinding
import com.example.noteappktor.other.Constanst.KEY_LOGGED_IN_EMAIL
import com.example.noteappktor.other.Constanst.KEY_PASSWORD
import com.example.noteappktor.other.Constanst.NO_EMAIL
import com.example.noteappktor.other.Constanst.NO_PASSWORD
import com.example.noteappktor.other.Status
import com.example.noteappktor.ui.BaseFragment
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class AuthFragment:BaseFragment(R.layout.fragment_auth) {
    private lateinit var binding: FragmentAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var sharedPref : SharedPreferences
    @Inject
    lateinit var basicAuthInterceptor: BasicAuthInterceptor

    private var currentEmail: String ? = null
    private var curPassword: String ? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        checkStatusLoggin()
        binding = FragmentAuthBinding.inflate(inflater,container,false)
        requireActivity().requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
        suscribeToObservers()
        registerUser()
        loggInUser()
        return binding.root
    }

    private fun checkStatusLoggin(){
        if (isLoggedIn()){
            authenticateApi(currentEmail ?: "",curPassword ?: "")
            redirectLogin()
        }
    }
    private fun isLoggedIn(): Boolean {
        currentEmail = sharedPref.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL) ?: NO_EMAIL
        curPassword = sharedPref.getString(KEY_PASSWORD, NO_PASSWORD) ?: NO_PASSWORD
        return currentEmail != NO_EMAIL && curPassword != NO_PASSWORD
    }


    private fun authenticateApi(email: String, password: String){
        basicAuthInterceptor.email = email
        basicAuthInterceptor.password = password

    }

    private fun redirectLogin(){
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.authFragment,true)
            .build()
        findNavController().navigate(
            AuthFragmentDirections.actionAuthFragmentToNotesFragment(),
            navOptions
        )
    }

    private fun loggInUser() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()
            currentEmail=email
            curPassword = password
            viewModel.login(email,password)
        }
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
        viewModel.loginStatus.observe(viewLifecycleOwner){ result ->
            result?.let {
                when(result.status){
                    Status.SUCCESS->{
                        binding.loginProgressBar.isVisible = false
                        showSnackbar(result.data ?: "Loggin exitoso")
                        sharedPref.edit().putString(KEY_LOGGED_IN_EMAIL,currentEmail).apply()
                        sharedPref.edit().putString(KEY_PASSWORD,curPassword).apply()
                        authenticateApi(currentEmail?: "", curPassword?: "")
                        Timber.d("CALlED")
                        redirectLogin()
                    }
                    Status.ERROR->{
                        binding.loginProgressBar.isVisible = false
                        showSnackbar(result.message ?: "Un error ocurrio")
                    }
                    Status.LOADING->{
                        binding.loginProgressBar.isVisible = true
                    }
                }
            }

        }



        viewModel.registerStatus.observe(viewLifecycleOwner){ result ->
            result?.let {
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
}