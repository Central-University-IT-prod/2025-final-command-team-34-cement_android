package com.itblaze.mentor.data.models.api.requests.requests.post

data class Tag(
    val id: Int,
    val name: String
)

data class Student(
    val id: Int,
    val login: String,
    val tg: String,
    val description: String,
    val course: Int,
    val role: String,
    val fio: String,
    val profileImage: String?
)

data class Mentor(
    val id: Int,
    val login: String,
    val tg: String,
    val description: String,
    val course: Int,
    val role: String,
    val fio: String,
    val profileImage: String?,
    val mentorRating: Double,
    val tags: List<Tag>
)

data class PostRequestResponse(
    val id: Int,
    val student: Student,
    val mentor: Mentor,
    val tags: List<String>,
    val tagsIds: List<Int>,
    val problem: String,
    val status: String = "in_process"
)

//data class PostRequestResponse(
//    val id: Long,
//    val student: Student,
//    val mentor: Mentor,
//    val tags: List<Int>,
//    val problem: String,
//    val status: String = "in_process"
//)
