package com.example.noteappktor.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.noteappktor.data.local.NotesDatabase
import com.example.noteappktor.data.remote.BasicAuthInterceptor
import com.example.noteappktor.data.remote.NoteApi
import com.example.noteappktor.other.Constanst.BASE_URL
import com.example.noteappktor.other.Constanst.DATABASE_NAME
import com.example.noteappktor.other.Constanst.ENCRYPTED_SHARED_PREF_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * cada modulo que dagger crea necesita ser instalado en un}
 * componenete especifico, el componente determina que tanta
 * duracion de vida tiene nuestro componente
 * Por ejemplo nuestra base de datos deberia vivir  el siclo
 * de vida de la applicacion entero por lo que deben ser singlentons
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNotesDatabase(
        @ApplicationContext contex: Context
    )= Room.databaseBuilder(contex,NotesDatabase::class.java,DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideNotesDao(db: NotesDatabase) = db.noteDao()

    @Singleton
    @Provides
    fun provideBasicAuthInterceptor() = BasicAuthInterceptor()

    @Singleton
    @Provides
    fun provideNoteApi(basicAuthInterceptor: BasicAuthInterceptor): NoteApi{
        val client = OkHttpClient.Builder()
            .addInterceptor(basicAuthInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NoteApi::class.java)
    }

    @Singleton
    @Provides
    fun provideEncryptedSharedPreferences(
        @ApplicationContext contex: Context
    ): SharedPreferences {
        val masterKeys = MasterKey.Builder(contex)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            contex,
            ENCRYPTED_SHARED_PREF_NAME,
            masterKeys,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

}