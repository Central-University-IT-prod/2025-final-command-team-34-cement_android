package com.itblaze.mentor.data.models.api.requests.requests.post

data class PostRequestsRequest(
    val mentor: Long,
    val tags: List<Int>,
    val problem: String
)
