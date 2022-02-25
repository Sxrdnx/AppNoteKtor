package com.example.noteappktor.ui.notedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteappktor.R
import com.example.noteappktor.data.local.entities.Note
import com.example.noteappktor.databinding.FragmentNoteDetailBinding
import com.example.noteappktor.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
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
        binding = FragmentNoteDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subcribeToObserver()
        binding.fabEditNote.setOnClickListener {
            findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToAddEditNoteFragment(args.id))
        }
    }

    private fun setMarkdownText(text: String){
        val markwon = Markwon.create(requireContext())
        val markdown = markwon.toMarkdown(text)
        markwon.setParsedMarkdown(binding.tvNoteContent,markdown)
    }

    private fun subcribeToObserver(){
        viewModel.observerNoteById(args.id).observe(viewLifecycleOwner){
            it?.let {note ->
                binding.tvNoteTitle.text = note.title
                setMarkdownText(note.content)
                curNote = note
            }?: showSnackbar("nota no encontrada")
        }
    }
}