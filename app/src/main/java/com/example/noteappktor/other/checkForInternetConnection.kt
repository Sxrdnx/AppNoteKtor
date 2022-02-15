package com.example.noteappktor.other

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build

fun checkForInternetConnection(context: Context): Boolean {
    val conectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        as ConnectivityManager
 return false
}