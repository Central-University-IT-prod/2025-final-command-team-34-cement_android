package com.itblaze.mentor.data.view.model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.itblaze.mentor.data.models.api.requests.tag.get.TagsResponse
import com.itblaze.mentor.data.models.api.requests.tag.id.TagIdRequest
import com.itblaze.mentor.data.models.api.requests.tag.id.TagIdResponse
import com.itblaze.mentor.data.repository.TagRepository
import com.itblaze.mentor.data.util.Constants.Companion.NO_INTERNET_CONNECTION
import com.itblaze.mentor.data.util.Constants.Companion.NO_SIGNAL
import com.itblaze.mentor.data.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class TagViewModel(
    app: Application,
    private val repository: TagRepository
) : AndroidViewModel(app) {

    val tagsData: MutableLiveData<Resource<List<TagsResponse>>> = MutableLiveData()
    val tagData: MutableLiveData<Resource<TagIdResponse>> = MutableLiveData() // Поле для хранения данных о теге

    fun getTags() = viewModelScope.launch {
        getTagsInternet()
    }

    fun getTagById(id: TagIdRequest) = viewModelScope.launch {
        getTagByIdInternet(id)
    }

    private suspend fun getTagsInternet() {
        tagsData.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repository.getTags()
                tagsData.postValue(handleTagsResponse(response))
            } else {
                tagsData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> tagsData.postValue(Resource.Error(t.message.toString()))
                else -> tagsData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private suspend fun getTagByIdInternet(id: TagIdRequest) {
        tagData.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repository.getTagById(id) // Вызов функции из репозитория
                tagData.postValue(handleTagResponse(response)) // Обработка ответа
            } else {
                tagData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> tagData.postValue(Resource.Error(t.message.toString()))
                else -> tagData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private fun handleTagsResponse(response: Response<List<TagsResponse>>): Resource<List<TagsResponse>> {
        return if (response.isSuccessful) {
            response.body()?.let { Resource.Success(it) } ?: Resource.Error("Empty response")
        } else {
            Resource.Error(response.message())
        }
    }

    private fun handleTagResponse(response: Response<TagIdResponse>): Resource<TagIdResponse> {
        return if (response.isSuccessful) {
            response.body()?.let { Resource.Success(it) } ?: Resource.Error("Empty response")
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

