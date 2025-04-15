//package com.itblaze.mentor.data.models
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import com.itblaze.mentor.data.util.Role
//
//@Entity(tableName = "MentorProfileItems")
//data class MentorProfile(
//    @PrimaryKey
//    val id: Int? = null,
//    var fio: String,
//    var description: String? = null,
//    var course: Int,
//    val login: String,
//    var tg: String,
//    var role: String = Role.Mentor.role,
//    var tag: List<String>,
//    var rating: Double
//)