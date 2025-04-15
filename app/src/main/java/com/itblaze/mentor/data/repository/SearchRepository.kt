package com.itblaze.mentor.data.repository

import com.itblaze.mentor.data.api.RetrofitInstance
import com.itblaze.mentor.data.models.api.requests.search.SearchRequest

class SearchRepository {
    suspend fun searchMentors(tags: SearchRequest) = RetrofitInstance.searchAPI.searchMentors(
        tags = tags.tagId,
        problem = tags.problem
    )
}