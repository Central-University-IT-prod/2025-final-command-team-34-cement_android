package com.itblaze.mentor.data.models.api.requests.user.login

// login in path get
data class LoginUserResponse(
    val id: Long,
    val login: String,
    val tg: String,
    val description: String,
    val course: Long,
    val role: String,
    val fio: String,
    val profileImage: String?
)
