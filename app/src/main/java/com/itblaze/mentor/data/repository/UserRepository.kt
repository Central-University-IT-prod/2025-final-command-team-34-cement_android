package com.itblaze.mentor.data.repository

import com.itblaze.mentor.data.api.RetrofitInstance
import com.itblaze.mentor.data.models.api.requests.user.change.password.ChangePasswordRequest
import com.itblaze.mentor.data.models.api.requests.user.login.LoginUserRequest
import com.itblaze.mentor.data.models.api.requests.user.patch.PatchUserRequest

class UserRepository {
    suspend fun changePassword(request: ChangePasswordRequest) = RetrofitInstance.userAPI.changePassword(request)
    suspend fun getCurrentUser() = RetrofitInstance.userAPI.getCurrentUser()
    suspend fun updateUser(request: PatchUserRequest) = RetrofitInstance.userAPI.updateUser(request)
    suspend fun getUserByLogin(login: LoginUserRequest) = RetrofitInstance.userAPI.getUserByLogin(login.login)
}