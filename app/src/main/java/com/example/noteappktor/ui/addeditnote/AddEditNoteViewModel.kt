package com.example.noteappktor.ui.addeditnote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappktor.data.local.entities.Note
import com.example.noteappktor.other.Event
import com.example.noteappktor.other.Resource
import com.example.noteappktor.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
private val notesRepository: NoteRepository
):ViewModel() {
    private val _note = MutableLiveData<Event<Resource<Note>>>()
    val note : LiveData<Event<Resource<Note>>> = _note

    @OptIn(DelicateCoroutinesApi::class)
    fun insertNote(note: Note) = GlobalScope.launch {
        notesRepository.insertNote(note)
    }

    fun getNotesById(noteID: String) = viewModelScope.launch {
        _note.postValue(Event(Resource.loading(null)))
        val note = notesRepository.getNoteById(noteID)
        note?.let {
            _note.postValue(Event(Resource.success(it)))
        } ?: _note.postValue(Event(Resource.error("Nota no encontrada",null)))
    }
}