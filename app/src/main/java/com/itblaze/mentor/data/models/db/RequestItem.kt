package com.itblaze.mentor.data.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itblaze.mentor.data.models.api.requests.requests.post.Tag

@Entity(tableName = "requestItems")
data class RequestItem(
    @PrimaryKey
    val dbId: Int? = null,
    val id: Long,
    val student: StudentForRequest,
    val mentor: MentorForRequest,
    val tags: List<Int>,
    val problem: String,
    val status: String
)

data class StudentForRequest(
    val id: Int,
    val login: String,
    val tg: String,
    val description: String,
    val course: Int,
    val role: String,
    val fio: String,
    val profileImage: String?
)

data class MentorForRequest(
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

