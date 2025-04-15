package com.itblaze.mentor.data.repository

import com.itblaze.mentor.data.api.RetrofitInstance
import com.itblaze.mentor.data.models.api.requests.authorization.login.LoginRequest
import com.itblaze.mentor.data.models.api.requests.authorization.signup.mentor.SignupMentorRequest
import com.itblaze.mentor.data.models.api.requests.authorization.signup.student.SignupStudentRequest

class AuthorizationRepository {

    suspend fun login(request: LoginRequest) =
        RetrofitInstance.authorizationAPI.login(request)

    suspend fun signupStudent(profile: SignupStudentRequest) =
        RetrofitInstance.authorizationAPI.signupStudent(profile)

    suspend fun signupMentor(profile: SignupMentorRequest) =
        RetrofitInstance.authorizationAPI.signupMentor(profile)
}