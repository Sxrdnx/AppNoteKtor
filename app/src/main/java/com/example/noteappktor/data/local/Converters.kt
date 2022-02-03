package com.example.noteappktor.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Clase para convertir el parametro de lista de propietarios
 * a json(string) y dice versa pues room no se puede definir una lista de
 * Strings
 */
class Converters {
     @TypeConverter
    fun fromList(list: List<String>):String{
        return Gson().toJson(list)
    }

    /**
     * para convertir el json(string) en una lista se debe definir una
     * manera que Gson sepa a que tipo queremos convertir , por lo que se
     * usa TypeToken
     */
    @TypeConverter
    fun toList(string: String):List<String>{
        return Gson().fromJson(string,object :TypeToken<List<String>>(){}.type)
    }

}