package com.example.noteappktor.data.remote

import com.example.noteappktor.data.local.entities.Note
import com.example.noteappktor.data.remote.requests.AccountRequest
import com.example.noteappktor.data.remote.requests.AddOwnerRequest
import com.example.noteappktor.data.remote.requests.DeleteNoteRequest
import com.example.noteappktor.data.remote.responses.SimpleResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * ResponseBody es la respuesta por defecto si es que
 * el servidor no nos responde
 */
interface NoteApi {
    @POST("/register")
    suspend fun register(
        @Body registerRequest: AccountRequest
    ): Response<SimpleResponse>


    @POST("/login")
    suspend fun login(
        @Body loginRequest: AccountRequest
    ): Response<SimpleResponse>


    @POST("/addNote")
    suspend fun addNote(
        @Body note: Note
    ): Response<RequestBody>


    @POST("/deleteNote")
    suspend fun addNote(
        @Body deleteNoteRequest: DeleteNoteRequest
    ): Response<RequestBody>


    @POST("/addOwnerToNote")
    suspend fun addOwnerTOnOTe(
        @Body addOwnerRequest: AddOwnerRequest
    ): Response<SimpleResponse>

    @GET("/getNotes")
    suspend fun getNotes(): Response<List<Note>>

}