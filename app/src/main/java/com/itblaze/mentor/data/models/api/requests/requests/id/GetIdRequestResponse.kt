package com.itblaze.mentor.data.models.api.requests.requests.id

data class Student(
    val id: Long,
    val login: String,
    val tg: String,
    val description: String,
    val course: Int,
    val role: String,
    val fio: String,
    val profileImage: String
)

data class Mentor(
    val id: Long,
    val login: String,
    val tg: String,
    val description: String,
    val course: Int,
    val role: String,
    val fio: String,
    val profileImage: String,
    val mentorRating: Int,
    val tags: List<String>
)

data class GetIdRequestResponse(
    val id: Long,
    val student: Student,
    val mentor: Mentor,
    val tags: List<String>,
    val problem: String,
    val status: String
)
