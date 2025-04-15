package com.itblaze.mentor.data.models.api.requests.user.get

data class GetUser(
    val id: Long,
    val login: String,
    val tg: String,
    val description: String,
    val course: Long,
    val role: String,
    val fio: String,
    val profileImage: String?
)
