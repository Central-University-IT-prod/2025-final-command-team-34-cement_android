package com.itblaze.mentor.data.models.api.requests.authorization.login

data class loginResponse(
    val token: String,
    val role: String
)
