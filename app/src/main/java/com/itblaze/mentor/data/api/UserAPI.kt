package com.itblaze.mentor.data.api

import com.itblaze.mentor.data.models.api.requests.user.change.password.ChangePasswordRequest
import com.itblaze.mentor.data.models.api.requests.user.change.password.ChangePasswordResponse
import com.itblaze.mentor.data.models.api.requests.user.get.GetUser
import com.itblaze.mentor.data.models.api.requests.user.login.LoginUserRequest
import com.itblaze.mentor.data.models.api.requests.user.login.LoginUserResponse
import com.itblaze.mentor.data.models.api.requests.user.patch.PatchUserRequest
import com.itblaze.mentor.data.models.api.requests.user.patch.PatchUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UserAPI {
    @POST("users/change_password/")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): Response<ChangePasswordResponse>

    @GET("users/me/")
    suspend fun getCurrentUser(): Response<GetUser>

    @PATCH("users/me/")
    suspend fun updateUser(
        @Body request: PatchUserRequest
    ): Response<PatchUserResponse>

    @GET("users/{login}/")
    suspend fun getUserByLogin(
        @Path("login") login: String
    ): Response<LoginUserResponse>
}