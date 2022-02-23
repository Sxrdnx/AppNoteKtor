package com.example.noteappktor.repositories

import android.app.Application
import com.example.noteappktor.data.local.NoteDao
import com.example.noteappktor.data.local.entities.Note
import com.example.noteappktor.data.remote.NoteApi
import com.example.noteappktor.data.remote.requests.AccountRequest
import com.example.noteappktor.other.Resource
import com.example.noteappktor.other.checkForInternetConnection
import com.example.noteappktor.other.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,
    private val context:Application
) {


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

    fun getAllNotes(): Flow<Resource<List<Note>>>{
        return networkBoundResource(
            query = {
                noteDao.getAllNotes()
            },
            fetch = {
                noteApi.getNotes()
            },
            saveFetchResult = { response ->
                response.body()?.let{
                   insertNotes(it)
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

    suspend fun deleteT() = noteDao.droptable()
}