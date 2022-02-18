package com.example.noteappktor.other

object Constanst {
    const val DATABASE_NAME = "notes_db"

    const val BASE_URL= "http://192.168.100.101:8080"

    const val  KEY_LOGGED_IN_EMAIL ="KEY_LOGGED_IN_EMAIL"

    const val KEY_PASSWORD = "KEY_PASSWORD"

    const val ENCRYPTED_SHARED_PREF_NAME = "enc_shared_pref"

    val IGNORE_AUTH_URLS = listOf("/login","/register")
}