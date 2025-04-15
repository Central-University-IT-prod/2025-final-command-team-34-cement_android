package com.itblaze.mentor.data.repository

import com.itblaze.mentor.data.api.RetrofitInstance
import com.itblaze.mentor.data.models.api.requests.mentors.score.MentorScoreRequest

class MentorRepository {
    suspend fun rateMentor(login: String, request: MentorScoreRequest) =
        RetrofitInstance.mentorsAPI.rateMentor(login, request)
}