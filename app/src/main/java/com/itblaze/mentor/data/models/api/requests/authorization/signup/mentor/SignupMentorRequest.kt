package com.itblaze.mentor.data.models.api.requests.authorization.signup.mentor

data class SignupMentorRequest(
    val login: String,
    val fio : String,
    val password: String,
    val tg: String,
    val role: String,
    val curse: Int,
    val description: String? = null,
    val teg: List<Int>
)