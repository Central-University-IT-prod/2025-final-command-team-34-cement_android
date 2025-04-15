package com.itblaze.mentor.data.api

import com.itblaze.mentor.data.models.api.requests.search.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAPI {

    @GET("search/")
    suspend fun searchMentors(
        @Query("tag") tags: List<Int>, // Параметр tag (может быть несколько)
        @Query("problem") problem: String?
    ): Response<List<SearchResponse>>
}