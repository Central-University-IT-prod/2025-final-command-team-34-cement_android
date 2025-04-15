package com.itblaze.mentor.data.view.model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.itblaze.mentor.data.db.MainDB
import com.itblaze.mentor.data.models.api.requests.requests.accept.AcceptRequestRequest
import com.itblaze.mentor.data.models.api.requests.requests.decline.DeclineRequestRequest
import com.itblaze.mentor.data.models.api.requests.requests.get.GetRequestResponse
import com.itblaze.mentor.data.models.api.requests.requests.id.GetIdRequestRequest
import com.itblaze.mentor.data.models.api.requests.requests.id.GetIdRequestResponse
import com.itblaze.mentor.data.models.api.requests.requests.post.PostRequestResponse
import com.itblaze.mentor.data.models.api.requests.requests.post.PostRequestsRequest
import com.itblaze.mentor.data.models.db.MentorForRequest
import com.itblaze.mentor.data.models.db.RequestItem
import com.itblaze.mentor.data.models.db.StudentForRequest
import com.itblaze.mentor.data.repository.RequestRepository
import com.itblaze.mentor.data.util.Constants.Companion.NO_INTERNET_CONNECTION
import com.itblaze.mentor.data.util.Constants.Companion.NO_SIGNAL
import com.itblaze.mentor.data.util.Resource
import com.itblaze.requests.data.view.model.DBRequestsViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class RequestViewModel(
    app: Application,
    private val repository: RequestRepository
) : AndroidViewModel(app) {

    val requestsData: MutableLiveData<Resource<List<GetRequestResponse>>> = MutableLiveData()
    val createRequestData: MutableLiveData<Resource<PostRequestResponse>> = MutableLiveData()
    val requestDetailsData: MutableLiveData<Resource<GetIdRequestResponse>> = MutableLiveData()
    val acceptRequestData: MutableLiveData<Resource<Unit>> =
        MutableLiveData() // Поле для принятия запроса
    val declineRequestData: MutableLiveData<Resource<Unit>> =
        MutableLiveData() // Поле для отклонения запроса

    init {
        getRequests()
    }

    fun getRequests() = viewModelScope.launch {
        getRequestsInternet()
    }

    fun createRequest(request: PostRequestsRequest) = viewModelScope.launch {
        createRequestInternet(request)
    }

    fun getRequestById(id: GetIdRequestRequest) = viewModelScope.launch {
        getRequestByIdInternet(id)
    }

    fun acceptRequest(id: AcceptRequestRequest) = viewModelScope.launch {
        acceptRequestInternet(id)
    }

    fun declineRequest(id: DeclineRequestRequest) = viewModelScope.launch {
        declineRequestInternet(id)
    }

    private suspend fun getRequestsInternet() {
        requestsData.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repository.getRequests()
                requestsData.postValue(handleRequestsResponse(response))
            } else {
                requestsData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> requestsData.postValue(Resource.Error(t.message.toString()))
                else -> requestsData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private suspend fun createRequestInternet(request: PostRequestsRequest) {
        createRequestData.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repository.createRequest(request)
                val res = handleCreateRequestResponse(response)
                if (response.isSuccessful) {
                    createRequestData.postValue(res)
                }
            } else {
                createRequestData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> createRequestData.postValue(Resource.Error(t.message.toString()))
                else -> createRequestData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private suspend fun getRequestByIdInternet(id: GetIdRequestRequest) {
        requestDetailsData.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repository.getRequestById(id)
                requestDetailsData.postValue(handleRequestDetailsResponse(response))
            } else {
                requestDetailsData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> requestDetailsData.postValue(Resource.Error(t.message.toString()))
                else -> requestDetailsData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private suspend fun acceptRequestInternet(id: AcceptRequestRequest) {
        acceptRequestData.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repository.acceptRequest(id) // Вызов функции из репозитория
                acceptRequestData.postValue(handleAcceptRequestResponse(response)) // Обработка ответа
            } else {
                acceptRequestData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> acceptRequestData.postValue(Resource.Error(t.message.toString()))
                else -> acceptRequestData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private suspend fun declineRequestInternet(id: DeclineRequestRequest) {
        declineRequestData.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repository.declineRequest(id) // Вызов функции из репозитория
                declineRequestData.postValue(handleDeclineRequestResponse(response)) // Обработка ответа
            } else {
                declineRequestData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> declineRequestData.postValue(Resource.Error(t.message.toString()))
                else -> declineRequestData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private fun handleRequestsResponse(response: Response<List<GetRequestResponse>>): Resource<List<GetRequestResponse>> {
        return if (response.isSuccessful) {
            response.body()?.let { Resource.Success(it) } ?: Resource.Error("Empty response")
        } else {
            Resource.Error(response.message())
        }
    }

    private fun handleCreateRequestResponse(response: Response<PostRequestResponse>): Resource<PostRequestResponse> {
        return if (response.isSuccessful) {
            response.body()?.let { Resource.Success(it) } ?: Resource.Error("Empty response")
        } else {
            Resource.Error(response.message())
        }
    }

    private fun handleRequestDetailsResponse(response: Response<GetIdRequestResponse>): Resource<GetIdRequestResponse> {
        return if (response.isSuccessful) {
            response.body()?.let { Resource.Success(it) } ?: Resource.Error("Empty response")
        } else {
            Resource.Error(response.message())
        }
    }

    private fun handleAcceptRequestResponse(response: Response<Unit>): Resource<Unit> {
        return if (response.isSuccessful) {
            Resource.Success(Unit) // Успешно принятый запрос
        } else {
            Resource.Error(response.message())
        }
    }

    private fun handleDeclineRequestResponse(response: Response<Unit>): Resource<Unit> {
        return if (response.isSuccessful) {
            Resource.Success(Unit) // Успешно отклоненный запрос
        } else {
            Resource.Error(response.message())
        }
    }

    private fun internetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }
}
