package com.itblaze.mentor.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.itblaze.mentor.data.models.db.MentorForRequest
import com.itblaze.mentor.data.models.db.StudentForRequest
import java.util.Date

internal class Converters {

    private val gson: Gson = GsonBuilder().create()

    // Converter for Date
    @TypeConverter
    fun fromDate(value: Date?): Long? {
        return value?.time
    }

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    // Converter for List<String>
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        return value?.split(",") ?: emptyList()
    }

    // Converter for StudentForRequest
    @TypeConverter
    fun fromStudentForRequest(student: StudentForRequest?): String? {
        return student?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toStudentForRequest(value: String?): StudentForRequest? {
        return value?.let { gson.fromJson(it, StudentForRequest::class.java) }
    }

    // Converter for MentorForRequest
    @TypeConverter
    fun fromMentorForRequest(mentor: MentorForRequest?): String? {
        return mentor?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toMentorForRequest(value: String?): MentorForRequest? {
        return value?.let { gson.fromJson(it, MentorForRequest::class.java) }
    }

    // Converter for List<Int>
    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int> {
        return value?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()
    }

    // Converter for List<Any>
    @TypeConverter
    fun fromAnyList(value: List<Any>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toAnyList(value: String?): List<Any>? {
        return value?.let { gson.fromJson(it, Array<Any>::class.java).toList() }
    }
}