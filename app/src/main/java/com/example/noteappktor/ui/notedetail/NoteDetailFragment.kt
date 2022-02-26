package com.example.noteappktor.ui.notedetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteappktor.R
import com.example.noteappktor.data.local.entities.Note
import com.example.noteappktor.databinding.FragmentNoteDetailBinding
import com.example.noteappktor.other.Resource
import com.example.noteappktor.other.Status
import com.example.noteappktor.ui.BaseFragment
import com.example.noteappktor.ui.dialogs.AddOwnerDialog
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
const val  ADD_OWNER_DIALOG_TAG = "ADD_OWNER_DIALOG_TAG"
@AndroidEntryPoint
class NoteDetailFragment:BaseFragment(R.layout.fragment_note_detail) {
    private lateinit var binding: FragmentNoteDetailBinding
    private val args: NoteDetailFragmentArgs by navArgs()
    private val viewModel: NoteDetailViewModel by viewModels()
    private var curNote: Note? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentNoteDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subcribeToObserver()
        binding.fabEditNote.setOnClickListener {
            findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToAddEditNoteFragment(args.id))
        }

        if (savedInstanceState != null ){
            val addOwnerDialog = parentFragmentManager.findFragmentByTag(ADD_OWNER_DIALOG_TAG) as AddOwnerDialog?
            addOwnerDialog?.setPositiveListenerListener {
                addOwnerToCurrentNote(it)
            }
        }
    }

    private fun showAddOwnerDialog(){
        AddOwnerDialog().apply {
            setPositiveListenerListener {
                addOwnerToCurrentNote(it)
            }
        }.show(parentFragmentManager,ADD_OWNER_DIALOG_TAG)

    }

    private fun addOwnerToCurrentNote(email: String){
        curNote?.let { note ->
            viewModel.addOwnerToNote(email,note.id)
        }
    }


    private fun setMarkdownText(text: String){
        val markwon = Markwon.create(requireContext())
        val markdown = markwon.toMarkdown(text)
        markwon.setParsedMarkdown(binding.tvNoteContent,markdown)
    }

    private fun subcribeToObserver(){
        viewModel.addOwnerStatus.observe(viewLifecycleOwner){event ->
            event?.getContentIfNotHandled()?.let {result ->
                when(result.status){
                    Status.SUCCESS->{
                        binding.addOwnerProgressBar.visibility = View.GONE
                        showSnackbar(result.data?: "usuario agregago correctamente")
                    }
                    Status.ERROR->{
                        binding.addOwnerProgressBar.visibility = View.GONE
                        showSnackbar(result.message?:"Erro inesperado ocurrio")
                    }
                    Status.LOADING->{
                        binding.addOwnerProgressBar.visibility = View.VISIBLE
                    }
                }
            }

        }

        viewModel.observerNoteById(args.id).observe(viewLifecycleOwner){
            it?.let {note ->
                binding.tvNoteTitle.text = note.title
                setMarkdownText(note.content)
                curNote = note
            }?: showSnackbar("nota no encontrada")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_detal_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miAddOwner-> showAddOwnerDialog()
        }
        return super.onOptionsItemSelected(item)
    }
}