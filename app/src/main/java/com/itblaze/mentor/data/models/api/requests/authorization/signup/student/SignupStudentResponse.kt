package com.itblaze.mentor.data.models.api.requests.authorization.signup.student

data class SignupStudentResponse(
    val login: String,
    val fio : String,
    val password: String,
    val tg: String,
    val role: String,
    val curse: Int,
    val description: String? = null,
    val token: String,
    val tags: List<Int>,
    val profileImage: String? = null
)
