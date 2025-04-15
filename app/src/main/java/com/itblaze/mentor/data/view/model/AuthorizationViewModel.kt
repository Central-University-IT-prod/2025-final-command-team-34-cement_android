package com.itblaze.mentor.data.view.model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.itblaze.mentor.data.models.api.requests.authorization.login.LoginRequest
import com.itblaze.mentor.data.models.api.requests.authorization.login.loginResponse
import com.itblaze.mentor.data.models.api.requests.authorization.signup.mentor.SignupMentorRequest
import com.itblaze.mentor.data.models.api.requests.authorization.signup.mentor.SignupMentorResponse
import com.itblaze.mentor.data.models.api.requests.authorization.signup.student.SignupStudentResponse
import com.itblaze.mentor.data.models.api.requests.authorization.signup.student.SignupStudentRequest
import com.itblaze.mentor.data.repository.AuthorizationRepository
import com.itblaze.mentor.data.util.Constants.Companion.NO_INTERNET_CONNECTION
import com.itblaze.mentor.data.util.Constants.Companion.NO_SIGNAL
import com.itblaze.mentor.data.util.Resource
import com.itblaze.mentor.data.util.Role
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class AuthorizationViewModel(
    app: Application,
    private val repository: AuthorizationRepository
) : AndroidViewModel(app) {

    val loginToken: MutableLiveData<Resource<loginResponse>> = MutableLiveData()

    private var loginResponseToken: loginResponse? = null

    val signupStudentData: MutableLiveData<Resource<SignupStudentResponse>> = MutableLiveData()

    private var signupStudentResponseProfile: SignupStudentResponse? = null

    val signupMentorData: MutableLiveData<Resource<SignupMentorResponse>> = MutableLiveData()

    private var signupMentorResponseProfile: SignupMentorResponse? = null

    fun login(login: String, password: String) =
        viewModelScope.launch {
            loginInternet(
                LoginRequest(
                    login,
                    password
                )
            )
        }

    fun signupStudent(
        login: String,
        password: String,
        tg: String,
        fio: String,
        curse: Int,
        description: String?,
    ) =
        viewModelScope.launch {
            signupStudentInternet(
                SignupStudentRequest(
                    login,
                    password,
                    tg,
                    fio,
                    Role.Student.role,
                    curse,
                    description
                )
            )
        }

    fun signupMentor(
        login: String,
        password: String,
        tg: String,
        fio: String,
        curse: Int,
        description: String?,
        tag: List<Int>
    ) =
        viewModelScope.launch {
            signupMentorInternet(
                SignupMentorRequest(
                    login,
                    password,
                    tg,
                    fio,
                    Role.Mentor.role,
                    curse,
                    description,
                    tag
                )
            )
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

    private fun loginResponse(response: Response<loginResponse>): Resource<loginResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                loginResponseToken = resultResponse
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun signupStudentResponse(response: Response<SignupStudentResponse>): Resource<SignupStudentResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                signupStudentResponseProfile = resultResponse
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun signupMentorResponse(response: Response<SignupMentorResponse>): Resource<SignupMentorResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                signupMentorResponseProfile = resultResponse
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun loginInternet(request: LoginRequest) {
        loginToken.postValue(Resource.Loading)
        try {
            if (internetConnection((this.getApplication()))) {
                val response = repository.login(request)
                loginToken.postValue(loginResponse(response))
            } else {
                loginToken.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> loginToken.postValue(Resource.Error(t.message.toString()))
                else -> loginToken.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private suspend fun signupStudentInternet(
        request: SignupStudentRequest
    ) {
        signupStudentData.postValue(Resource.Loading)
        try {
            if (internetConnection((this.getApplication()))) {
                val response = repository.signupStudent(
                    request
                )
                signupStudentData.postValue(signupStudentResponse(response))
            } else {
                signupStudentData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> signupStudentData.postValue(Resource.Error(t.message.toString()))
                else -> signupStudentData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private suspend fun signupMentorInternet(
        request: SignupMentorRequest
    ) {
        signupMentorData.postValue(Resource.Loading)
        try {
            if (internetConnection((this.getApplication()))) {
                val response = repository.signupMentor(
                    request
                )
                signupMentorData.postValue(signupMentorResponse(response))
            } else {
                signupMentorData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> signupMentorData.postValue(Resource.Error(t.message.toString()))
                else -> signupMentorData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }
}