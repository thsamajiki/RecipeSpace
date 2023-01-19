package com.hero.recipespace.ext

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hero.recipespace.data.message.MessageData
import java.util.*

object TypeConverterExt {
    @TypeConverter
    fun mapToString(value: Map<String, String>?): String {
        return if(value == null) "" else Gson().toJson(value)
    }

    @TypeConverter
    fun stringToMap(value: String): Map<String, String> {
        return Gson().fromJson(value,  object : TypeToken<Map<String, String>>() {}.type)
    }

    @TypeConverter
    fun mapToBoolean(value: Map<String, Boolean>?): String {
        return if(value == null) "" else Gson().toJson(value)
    }

    @TypeConverter
    fun booleanToMap(value: String): Map<String, Boolean> {
        return Gson().fromJson(value,  object : TypeToken<Map<String, Boolean>>() {}.type)
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String = Gson().toJson(value)

    @TypeConverter
    fun toStringList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()

    @TypeConverter
    fun fromFloatList(value: List<Float>?): String = Gson().toJson(value)

    @TypeConverter
    fun toFloatList(value: String) = Gson().fromJson(value, Array<Float>::class.java).toList()

    @TypeConverter
    fun fromMessageData(value: MessageData): String = Gson().toJson(value)

    @TypeConverter
    fun toMessageData(value: String): MessageData {
        return Gson().fromJson(value, object : TypeToken<MessageData>() {}.type)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? = if (value == null) {
        null
    } else {
        Timestamp(Date(value))
    }

    @TypeConverter
    fun toTimestamp(timestamp: Timestamp?): Long? = timestamp?.toDate()?.time
}