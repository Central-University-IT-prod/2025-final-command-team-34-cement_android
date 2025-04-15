package com.itblaze.mentor.data.repository

import com.itblaze.mentor.data.api.RetrofitInstance
import com.itblaze.mentor.data.models.api.requests.requests.accept.AcceptRequestRequest
import com.itblaze.mentor.data.models.api.requests.requests.decline.DeclineRequestRequest
import com.itblaze.mentor.data.models.api.requests.requests.id.GetIdRequestRequest
import com.itblaze.mentor.data.models.api.requests.requests.post.PostRequestsRequest

class RequestRepository {
    suspend fun getRequests() = RetrofitInstance.requestAPI.getRequests()
    suspend fun createRequest(request: PostRequestsRequest) =
        RetrofitInstance.requestAPI.createRequest(request)
    suspend fun getRequestById(id: GetIdRequestRequest) =
        RetrofitInstance.requestAPI.getRequestById(id.id)
    suspend fun acceptRequest(id: AcceptRequestRequest) =
        RetrofitInstance.requestAPI.acceptRequest(id.id)
    suspend fun declineRequest(id: DeclineRequestRequest) =
        RetrofitInstance.requestAPI.declineRequest(id.id)
}