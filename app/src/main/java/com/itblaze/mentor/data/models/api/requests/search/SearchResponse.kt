package com.itblaze.mentor.data.models.api.requests.search

data class SearchResponse(
    val id: Long,
    val login: String,
    val tg: String,
    val description: String,
    val course: Int,
    val role: String,
    val fio: String,
    val mentorRating: Int,
    val tags: List<Tag>,
    val aiScore: Double,
    val profileImage: String?
)

data class Tag(
    val id: Int,
    val name: String
)
