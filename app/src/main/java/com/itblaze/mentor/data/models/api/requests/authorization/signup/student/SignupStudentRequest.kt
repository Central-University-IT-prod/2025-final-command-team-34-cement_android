package com.itblaze.mentor.data.models.api.requests.authorization.signup.student

data class SignupStudentRequest(
    val login: String,
    val fio : String,
    val password: String,
    val tg: String,
    val role: String,
    val curse: Int,
    var description: String? = null,
)