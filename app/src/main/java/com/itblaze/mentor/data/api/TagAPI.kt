package com.itblaze.mentor.data.api

import com.itblaze.mentor.data.models.api.requests.tag.get.TagsResponse
import com.itblaze.mentor.data.models.api.requests.tag.id.TagIdRequest
import com.itblaze.mentor.data.models.api.requests.tag.id.TagIdResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TagAPI {
    // GET /tags/ — Получить список тегов
    @GET("tags/")
    suspend fun getTags(): Response<List<TagsResponse>>

    @GET("tags/{id}/")
    suspend fun getTagById(
        @Path("id") id: TagIdRequest
    ): Response<TagIdResponse>

}