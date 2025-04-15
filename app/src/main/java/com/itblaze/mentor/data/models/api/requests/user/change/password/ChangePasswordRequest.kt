package com.itblaze.mentor.data.models.api.requests.user.change.password

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)
