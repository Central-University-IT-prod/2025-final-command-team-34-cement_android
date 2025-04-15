package com.itblaze.mentor.data.models.api.requests.search

data class SearchRequest(
    val tagId: List<Int>,
    val problem: String? = null
)