package com.example.noteappktor.ui.addeditnote

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.noteappktor.R
import com.example.noteappktor.data.local.entities.Note
import com.example.noteappktor.databinding.FragmentAddEditNoteBinding
import com.example.noteappktor.other.Constanst.DEFAULT_NOTE_COLOR
import com.example.noteappktor.other.Constanst.KEY_LOGGED_IN_EMAIL
import com.example.noteappktor.other.Constanst.NO_EMAIL
import com.example.noteappktor.other.Resource
import com.example.noteappktor.other.Status
import com.example.noteappktor.ui.BaseFragment
import com.example.noteappktor.ui.dialogs.ColorPickerDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

const val FRAGMENT_TAG = "AddEditNoteFragment"
@AndroidEntryPoint
class AddEditNoteFragment: BaseFragment(R.layout.fragment_add_edit_note) {
    private val viewModel: AddEditNoteViewModel by viewModels()
    private val arg : AddEditNoteFragmentArgs by navArgs()
    private lateinit var binding : FragmentAddEditNoteBinding
    private var currentNote: Note? = null
    private var currentColor = DEFAULT_NOTE_COLOR
    @Inject
    lateinit var sharedPref : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditNoteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arg.id.isNotEmpty()){
            viewModel.getNotesById(arg.id)
            subscribeToObservers()
        }
        if (savedInstanceState != null ){
            val colorPickerDialog = parentFragmentManager.findFragmentByTag(FRAGMENT_TAG) as ColorPickerDialogFragment?
            colorPickerDialog?.setPositiveListenerListener {
                changeViewNoteColor(it)
            }
        }
        selectColor()

    }

    private fun selectColor(){
        binding.viewNoteColor.setOnClickListener {
            ColorPickerDialogFragment().apply {
                setPositiveListenerListener {
                    changeViewNoteColor(it)
                }
            }.show(parentFragmentManager,FRAGMENT_TAG)
        }
    }

    private fun changeViewNoteColor(colorString: String){
        val drawable = ResourcesCompat.getDrawable(this.resources,R.drawable.circle_shape,null)
        drawable?.let {
            val wrapperDrawable = DrawableCompat.wrap(it)
            val color = Color.parseColor("#$colorString")
            DrawableCompat.setTint(wrapperDrawable,color)
            binding.viewNoteColor.background = wrapperDrawable
            currentColor = colorString
        }
    }

    private fun subscribeToObservers() {
        viewModel.note.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { result->
                when(result.status){
                    Status.SUCCESS->{
                        val note = result.data!!
                        currentNote = note
                        binding.etNoteTitle.setText(note.title)
                        binding.etNoteContent.setText(note.content)
                        changeViewNoteColor(note.color)
                    }
                    Status.ERROR->{
                        showSnackbar(result.message?: "No se encontro la nota")
                    }
                    Status.LOADING->{}
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveNote()
    }

    private fun saveNote(){
        val authEmail = sharedPref.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL) ?: NO_EMAIL
        val tittle = binding.etNoteTitle.text.toString()
        val content = binding.etNoteContent.text.toString()
        if (tittle.isEmpty()||content.isEmpty())
            return

        val date = System.currentTimeMillis()
        val color = currentColor
        val id = currentNote?.id ?: UUID.randomUUID().toString()
        val owners = currentNote?.owners ?: listOf(authEmail)
        val note = Note(tittle,content,date,owners,color, id = id)
        viewModel.insertNote(note)

    }

}