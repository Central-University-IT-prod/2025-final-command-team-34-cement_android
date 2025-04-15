package com.itblaze.mentor.data.view.model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.itblaze.mentor.data.models.api.requests.mentors.score.MentorScoreRequest
import com.itblaze.mentor.data.repository.MentorRepository
import com.itblaze.mentor.data.util.Constants.Companion.NO_INTERNET_CONNECTION
import com.itblaze.mentor.data.util.Constants.Companion.NO_SIGNAL
import com.itblaze.mentor.data.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MentorsViewModel(
    app: Application,
    private val repository: MentorRepository
) : AndroidViewModel(app) {

    val ratingResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()

    private var mentorRating: Unit? = null

    fun rateMentor(login: String, score: Int) =
        viewModelScope.launch {
            rateMentorInternet(MentorScoreRequest(score, login), login)
        }

    private suspend fun rateMentorInternet(request: MentorScoreRequest, login: String) {
        ratingResponse.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repository.rateMentor(login, request)
                ratingResponse.postValue(handleRatingResponse(response))
            } else {
                ratingResponse.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> ratingResponse.postValue(Resource.Error(t.message.toString()))
                else -> ratingResponse.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private fun handleRatingResponse(response: Response<Unit>): Resource<Unit> {
        return if (response.isSuccessful) {
            Resource.Success(Unit)
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