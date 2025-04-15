package com.itblaze.mentor.data.api

import com.itblaze.mentor.data.models.api.requests.mentors.score.MentorScoreRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface MentorsAPI {

    @POST("mentors/{login}/rating")
    suspend fun rateMentor(
        @Path("login") login: String, // Captures the {login} path parameter
        @Body request: MentorScoreRequest // Request body containing the score
    ): Response<Unit> // Assuming the response is empty (HTTP 200 OK)
}