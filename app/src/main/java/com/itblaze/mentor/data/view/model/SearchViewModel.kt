package com.itblaze.mentor.data.view.model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.itblaze.mentor.data.models.api.requests.search.SearchRequest
import com.itblaze.mentor.data.models.api.requests.search.SearchResponse
import com.itblaze.mentor.data.repository.SearchRepository
import com.itblaze.mentor.data.util.Constants.Companion.NO_INTERNET_CONNECTION
import com.itblaze.mentor.data.util.Constants.Companion.NO_SIGNAL
import com.itblaze.mentor.data.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class SearchViewModel(
    app: Application,
    private val repository: SearchRepository
) : AndroidViewModel(app) {

    val searchResults: MutableLiveData<Resource<List<SearchResponse>>> = MutableLiveData()

    fun searchMentors(tags: SearchRequest) = viewModelScope.launch {
        searchMentorsInternet(tags)
    }

    private suspend fun searchMentorsInternet(tags: SearchRequest) {
        searchResults.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repository.searchMentors(tags)
                searchResults.postValue(handleSearchResponse(response))
            } else {
                searchResults.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchResults.postValue(Resource.Error(t.message.toString()))
                else -> searchResults.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private fun handleSearchResponse(response: Response<List<SearchResponse>>): Resource<List<SearchResponse>> {
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
