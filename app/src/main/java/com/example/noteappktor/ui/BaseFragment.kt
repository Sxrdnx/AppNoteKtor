package com.example.noteappktor.ui

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 * Clase que permite tener una base de funciones que
 * los demas fragments usaran, esto evita tener border place
 * inecesario
 */
abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    fun showSnackbar(text:String){
        Snackbar.make(
            requireActivity(),
            requireView(),
            text,
            Snackbar.LENGTH_LONG
        ).show()
    }

}