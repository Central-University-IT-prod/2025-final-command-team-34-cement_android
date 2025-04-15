package com.itblaze.mentor.data.models.api.requests.user.patch

data class PatchUserRequest(
    val login: String,
    val tg: String,
    val description: String,
    val course: Long,
    val fio: String
)
