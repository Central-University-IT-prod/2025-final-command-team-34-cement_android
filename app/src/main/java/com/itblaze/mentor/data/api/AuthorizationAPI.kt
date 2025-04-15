package com.itblaze.mentor.data.api

import com.itblaze.mentor.data.models.api.requests.authorization.login.LoginRequest
import com.itblaze.mentor.data.models.api.requests.authorization.signup.student.SignupStudentRequest
import com.itblaze.mentor.data.models.api.requests.authorization.signup.student.SignupStudentResponse
import com.itblaze.mentor.data.models.api.requests.authorization.login.loginResponse
import com.itblaze.mentor.data.models.api.requests.authorization.signup.mentor.SignupMentorRequest
import com.itblaze.mentor.data.models.api.requests.authorization.signup.mentor.SignupMentorResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorizationAPI {

    @POST("auth/login/")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<loginResponse>

    @POST("auth/")
    suspend fun signupStudent(
        @Body request: SignupStudentRequest
    ): Response<SignupStudentResponse>

    @POST("auth/")
    suspend fun signupMentor(
        @Body request: SignupMentorRequest
    ): Response<SignupMentorResponse>
}