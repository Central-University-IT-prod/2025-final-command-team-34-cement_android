package com.itblaze.mentor.data.api

import com.itblaze.mentor.data.models.api.requests.authorization.login.LoginRequest
import com.itblaze.mentor.data.models.api.requests.authorization.login.loginResponse
import com.itblaze.mentor.data.models.api.requests.authorization.signup.mentor.SignupMentorRequest
import com.itblaze.mentor.data.models.api.requests.authorization.signup.mentor.SignupMentorResponse
import com.itblaze.mentor.data.models.api.requests.authorization.signup.student.SignupStudentRequest
import com.itblaze.mentor.data.models.api.requests.authorization.signup.student.SignupStudentResponse
import com.itblaze.mentor.data.models.api.requests.requests.accept.AcceptRequestRequest
import com.itblaze.mentor.data.models.api.requests.requests.decline.DeclineRequestRequest
import com.itblaze.mentor.data.models.api.requests.requests.get.GetRequestResponse
import com.itblaze.mentor.data.models.api.requests.requests.id.GetIdRequestRequest
import com.itblaze.mentor.data.models.api.requests.requests.id.GetIdRequestResponse
import com.itblaze.mentor.data.models.api.requests.requests.post.PostRequestResponse
import com.itblaze.mentor.data.models.api.requests.requests.post.PostRequestsRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RequestAPI {

    // GET /requests/ — Получить список запросов
    @GET("requests/")
    suspend fun getRequests(): Response<List<GetRequestResponse>>

    // POST /requests/ — Создать новый запрос
    @POST("requests/")
    suspend fun createRequest(
        @Body request: PostRequestsRequest
    ): Response<PostRequestResponse>

    // GET /requests/{id}/ — Получить детали запроса по ID
    @GET("requests/{id}/")
    suspend fun getRequestById(
        @Path("id") id: Int
    ): Response<GetIdRequestResponse>

    // POST /requests/{id}/accept/ — Принять запрос
    @POST("requests/{id}/accept/")
    suspend fun acceptRequest(
        @Path("id") id: Int
    ): Response<Unit>

    // POST /requests/{id}/decline/ — Отклонить запрос
    @POST("requests/{id}/decline/")
    suspend fun declineRequest(
        @Path("id") id: Int
    ): Response<Unit>
}