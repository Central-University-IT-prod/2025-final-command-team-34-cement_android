package com.itblaze.mentor.data.repository

import com.itblaze.mentor.data.api.RetrofitInstance
import com.itblaze.mentor.data.models.api.requests.tag.id.TagIdRequest

class TagRepository {
    suspend fun getTags() = RetrofitInstance.tagAPI.getTags()
    suspend fun getTagById(id: TagIdRequest) = RetrofitInstance.tagAPI.getTagById(id)
}