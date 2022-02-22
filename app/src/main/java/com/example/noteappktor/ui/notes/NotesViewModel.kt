package com.example.noteappktor.ui.notes

import androidx.lifecycle.*
import com.example.noteappktor.data.local.entities.Note
import com.example.noteappktor.other.Event
import com.example.noteappktor.other.Resource
import com.example.noteappktor.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository
):ViewModel() {

    private val _forceUpdate = MutableLiveData(false)

    private val _allNotes = _forceUpdate.switchMap {
         noteRepository.getAllNotes().asLiveData(viewModelScope.coroutineContext)
    }.switchMap {
        MutableLiveData(Event(it))
    }
    val allNotes : LiveData<Event<Resource<List<Note>>>> = _allNotes


    fun syncAllNotes() = _forceUpdate.postValue(true)
}