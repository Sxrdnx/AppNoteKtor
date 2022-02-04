package com.example.noteappktor.data.remote.requests

data class AddOwnerRequest(
    val owner: String,
    val noteID: String
)
