package com.example.noteappktor.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.noteappktor.R
import com.example.noteappktor.databinding.EditTextEmailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class AddOwnerDialog: DialogFragment(){
    private lateinit var  binding: EditTextEmailBinding
    private var positiveListener: ((String)-> Unit) ? = null

    fun setPositiveListenerListener(listener: (String) ->Unit){
        positiveListener = listener
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = EditTextEmailBinding.inflate(layoutInflater)
        return MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.ic_add_person)
            .setTitle("Agregar Propietario")
            .setMessage("Agrege el correo de la persona con quien quisieras compartir la nota." +
                    "Esta persona podra ver y editar la nota")
            .setView(binding.root)
            .setPositiveButton("Agregar"){_,_ ->
                val email = binding.etAddOwnerEmail.text.toString()
                positiveListener?.let { yes ->
                    yes(email)
                }
            }
            .setNegativeButton("Cancelar"){dialogInterface , _ ->
                dialogInterface.cancel()
            }.create()
    }
}