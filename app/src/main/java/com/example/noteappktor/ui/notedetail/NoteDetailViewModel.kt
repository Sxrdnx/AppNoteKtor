package com.example.noteappktor.ui.notedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappktor.other.Event
import com.example.noteappktor.other.Resource
import com.example.noteappktor.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel@Inject constructor(
    private val noteRepository: NoteRepository
):ViewModel() {

    private val _addOwnerStatus= MutableLiveData<Event<Resource<String>>>()
    val addOwnerStatus : LiveData<Event<Resource<String>>> = _addOwnerStatus

    fun addOwnerToNote(owner:String, noteID: String){
        _addOwnerStatus.postValue(Event(Resource.loading(null)))
        if (owner.isEmpty() || noteID.isEmpty()){
            _addOwnerStatus.postValue(Event(Resource.error("ingrese un usuario",null)))
            return
        }
        viewModelScope.launch {
            val result = noteRepository.addOwnerToNote(owner,noteID)
            _addOwnerStatus.postValue(Event(result))
        }
    }


    fun observerNoteById(noteID: String) = noteRepository.observerNoteByID(noteID)

}