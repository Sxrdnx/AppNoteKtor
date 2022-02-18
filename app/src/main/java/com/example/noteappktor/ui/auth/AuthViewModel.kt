package com.example.noteappktor.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappktor.other.Resource
import com.example.noteappktor.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: NoteRepository
) :ViewModel() {

    private val _registerStatus = MutableLiveData<Resource<String>>()
    val registerStatus : LiveData<Resource<String>> get() = _registerStatus


    private val _loginStatus = MutableLiveData<Resource<String>>()
    val loginStatus : LiveData<Resource<String>> get() = _loginStatus

    fun login(email: String, password: String){
        _loginStatus.postValue(Resource.loading(null))
        if(email.isEmpty() || password.isEmpty()){
            _loginStatus.postValue(Resource.error( "Rellene todos los campos",null))
            return
        }

        viewModelScope.launch {
            val result = repository.login(email,password)
            _loginStatus.postValue(result)
        }

    }
    fun register(email: String, password: String, repeatPassword:String){

        _registerStatus.postValue(Resource.loading(null))
        if(email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
            _registerStatus.postValue(Resource.error( "Rellene todos los campos",null))
            return
        }

        if (password != repeatPassword){
            _registerStatus.postValue(Resource.error( "Las contraseñas no son iguales",null))
            return
        }

        viewModelScope.launch {
            val result = repository.register(email,password)
            _registerStatus.postValue(result)
        }

    }
}