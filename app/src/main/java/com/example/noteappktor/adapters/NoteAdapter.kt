package com.example.noteappktor.adapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappktor.R
import com.example.noteappktor.data.local.entities.Note
import com.example.noteappktor.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter: RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(private val itemNoteBinding: ItemNoteBinding ): RecyclerView.ViewHolder(itemNoteBinding.root){
        fun bind(note: Note){
            itemNoteBinding.tvTitle.text = note.title
            if (!note.isSynced){
                itemNoteBinding.ivSynced.setImageResource(R.drawable.ic_cross)
                itemNoteBinding.tvSynced.text = "No sincronizada"
            }else{
                itemNoteBinding.ivSynced.setImageResource(R.drawable.ic_check)
                itemNoteBinding.tvSynced.text = "sincronizada"
            }

            val dateFormat = SimpleDateFormat("dd.MM.yy,HH:mm",Locale.getDefault())
            val dataString = dateFormat.format(note.date)
            itemNoteBinding.tvDate.text = dataString

            val drawable = ResourcesCompat.getDrawable(itemNoteBinding.root.context.resources,R.drawable.circle_shape,null)
            drawable?.let {
                val wrapperDrawable = DrawableCompat.wrap(it)
                val color = Color.parseColor("#${note.color}")
                DrawableCompat.setTint(wrapperDrawable,color)
                itemNoteBinding.viewNoteColor.background = it
            }

            itemNoteBinding.root.setOnClickListener {
                onItemClickListener?.let { click->
                    click(note)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding: ItemNoteBinding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount() = notes.size

    private val diffCallback= object: DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Note, newItem: Note)= oldItem.hashCode() == newItem.hashCode()

    }

    private val differ = AsyncListDiffer(this,diffCallback)

    var notes:List<Note>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private var onItemClickListener : ((Note)-> Unit) ? = null

    fun setOnItemClickListener(onItemClick: (Note) -> Unit){
        this.onItemClickListener = onItemClick
    }
}