package com.example.noteappktor.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.noteappktor.data.local.entities.Note

@Database(
    entities = [Note::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class NotesDatabase:RoomDatabase() {

    abstract fun noteDao(): NoteDao

}