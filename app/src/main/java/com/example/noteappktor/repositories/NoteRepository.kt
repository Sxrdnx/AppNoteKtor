package com.example.noteappktor.repositories

import android.app.Application
import com.example.noteappktor.data.local.NoteDao
import com.example.noteappktor.data.local.entities.LocallyDeletedNoteID
import com.example.noteappktor.data.local.entities.Note
import com.example.noteappktor.data.remote.NoteApi
import com.example.noteappktor.data.remote.requests.AccountRequest
import com.example.noteappktor.data.remote.requests.DeleteNoteRequest
import com.example.noteappktor.other.Resource
import com.example.noteappktor.other.checkForInternetConnection
import com.example.noteappktor.other.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,
    private val context:Application
) {

    private var curNotesResponse: Response<List<Note>> ? = null

    
    suspend fun syncNotes(){
        val locallyDeletedNoteIDs = noteDao.getAllLocallyDeletedNoteIDs()
        locallyDeletedNoteIDs.forEach { id -> deleteNote(id.deletedNoteID) }

        val unsyncedNotes  = noteDao.getAllUnsyncedNotes()
        unsyncedNotes.forEach { note-> insertNote(note) }

        curNotesResponse = noteApi.getNotes()
        curNotesResponse?.body()?.let { notes->
            noteDao.deleteAllNotes()
            insertNotes(notes.onEach { note-> note.isSynced = true })
        }
    }

    suspend fun getNoteById(noteID: String) = noteDao.getNoteById(noteID)

    suspend fun insertNote(note: Note){
        val response = try {
            noteApi.addNote(note)
        }catch (e: Exception){
            println(e.message)
            null
        }
        if (response != null && response.isSuccessful){
            noteDao.insertNote(note.apply { isSynced = true  })
        }else{
            noteDao.insertNote(note)
        }
    }

    private suspend fun insertNotes(notes: List<Note>){
        notes.forEach {
            insertNote(it)
        }
    }

    suspend fun deleteNote(noteID: String){
        val response= try{
            noteApi.deleteNote(DeleteNoteRequest(noteID))
        }catch (e: Exception){null}
        noteDao.deleteNoteById(noteID)
        if (response== null || !response.isSuccessful){
            noteDao.insertLocallyDeleteNoteID(LocallyDeletedNoteID(noteID))
        }else{
            noteDao.deleteLocallyDeletedNoteID(noteID)
        }
    }


    suspend fun deleteLocallyDeletedNotedID(deletedNoteID: String){
        noteDao.deleteLocallyDeletedNoteID(deletedNoteID)
    }

    fun getAllNotes(): Flow<Resource<List<Note>>>{
        return networkBoundResource(
            query = {
                noteDao.getAllNotes()
            },
            fetch = {
                syncNotes()
                curNotesResponse
            },
            saveFetchResult = { response ->
                response?.body()?.let{
                   insertNotes(it.onEach { note -> note.isSynced = true })
                }
            },
            shouldFetch = {
                checkForInternetConnection(context)
            }
        )
    }

    suspend fun login(email: String, password: String)= withContext(Dispatchers.IO){
        try {
            val response = noteApi.login(AccountRequest(email,password))
            if (response.isSuccessful && response.body()!!.successful){
                Resource.success(response.body()?.message)
            }else{
                Resource.error(response.body()?.message ?: response.message(),null)
            }
        }catch (e: Exception){
            Resource.error("No es posible conectar al servidor. Revisa tu coneccion a internet",null)
        }
    }

    suspend fun register(email: String, password: String)= withContext(Dispatchers.IO){
        try {
            val response = noteApi.register(AccountRequest(email,password))
            if (response.isSuccessful && response.body()!!.successful){
                Resource.success(response.body()?.message)
            }else{
                Resource.error(response.body()?.message ?: response.message(),null)
            }
        }catch (e: Exception){
            Resource.error("No es posible conectar al servidor. Revisa tu coneccion a internet",null)
        }
    }
}