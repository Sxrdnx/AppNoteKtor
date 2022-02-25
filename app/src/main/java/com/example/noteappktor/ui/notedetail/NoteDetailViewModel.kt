package com.example.noteappktor.ui.notedetail

import androidx.lifecycle.ViewModel
import com.example.noteappktor.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel@Inject constructor(
    private val noteRepository: NoteRepository
):ViewModel() {

    fun observerNoteById(noteID: String) = noteRepository.observerNoteByID(noteID)

}