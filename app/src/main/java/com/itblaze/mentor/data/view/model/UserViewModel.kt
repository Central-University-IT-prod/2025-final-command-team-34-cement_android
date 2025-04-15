package com.itblaze.mentor.data.view.model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.itblaze.mentor.data.models.api.requests.user.change.password.ChangePasswordRequest
import com.itblaze.mentor.data.models.api.requests.user.change.password.ChangePasswordResponse
import com.itblaze.mentor.data.models.api.requests.user.get.GetUser
import com.itblaze.mentor.data.models.api.requests.user.login.LoginUserRequest
import com.itblaze.mentor.data.models.api.requests.user.login.LoginUserResponse
import com.itblaze.mentor.data.models.api.requests.user.patch.PatchUserRequest
import com.itblaze.mentor.data.models.api.requests.user.patch.PatchUserResponse
import com.itblaze.mentor.data.repository.UserRepository
import com.itblaze.mentor.data.util.Constants.Companion.NO_INTERNET_CONNECTION
import com.itblaze.mentor.data.util.Constants.Companion.NO_SIGNAL
import com.itblaze.mentor.data.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class UserViewModel(
    app: Application,
    private val repository: UserRepository
) : AndroidViewModel(app) {

    val userData: MutableLiveData<Resource<GetUser>> = MutableLiveData()
    val updateUserData: MutableLiveData<Resource<PatchUserResponse>> = MutableLiveData()
    val changePasswordData: MutableLiveData<Resource<ChangePasswordResponse>> = MutableLiveData()
    val loginUserData: MutableLiveData<Resource<LoginUserResponse>> = MutableLiveData() // Добавлено поле для логина

    fun getCurrentUser() = viewModelScope.launch {
        getCurrentUserInternet()
    }

    fun updateUser(request: PatchUserRequest) = viewModelScope.launch {
        updateUserInternet(request)
    }

    fun changePassword(request: ChangePasswordRequest) = viewModelScope.launch {
        changePasswordInternet(request)
    }

    // Новый метод для получения пользователя по логину
    fun getUserByLogin(login: LoginUserRequest) = viewModelScope.launch {
        getUserByLoginInternet(login)
    }

    private suspend fun getCurrentUserInternet() {
        userData.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repository.getCurrentUser()
                userData.postValue(handleUserResponse(response))
            } else {
                userData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> userData.postValue(Resource.Error(t.message.toString()))
                else -> userData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private suspend fun updateUserInternet(request: PatchUserRequest) {
        updateUserData.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repository.updateUser(request)
                updateUserData.postValue(handleUpdateUserResponse(response))
            } else {
                updateUserData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> updateUserData.postValue(Resource.Error(t.message.toString()))
                else -> updateUserData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private suspend fun changePasswordInternet(request: ChangePasswordRequest) {
        changePasswordData.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repository.changePassword(request)
                changePasswordData.postValue(handleChangePasswordResponse(response))
            } else {
                changePasswordData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> changePasswordData.postValue(Resource.Error(t.message.toString()))
                else -> changePasswordData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private suspend fun getUserByLoginInternet(login: LoginUserRequest) {
        loginUserData.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repository.getUserByLogin(login) // Вызов функции из репозитория
                loginUserData.postValue(handleLoginUserResponse(response)) // Обработка ответа
            } else {
                loginUserData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> loginUserData.postValue(Resource.Error(t.message.toString()))
                else -> loginUserData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private fun handleUserResponse(response: Response<GetUser>): Resource<GetUser> {
        return if (response.isSuccessful) {
            response.body()?.let { Resource.Success(it) } ?: Resource.Error("Empty response")
        } else {
            Resource.Error(response.message())
        }
    }

    private fun handleLoginUserResponse(response: Response<LoginUserResponse>): Resource<LoginUserResponse> {
        return if (response.isSuccessful) {
            response.body()?.let { Resource.Success(it) } ?: Resource.Error("Empty response")
        } else {
            Resource.Error(response.message())
        }
    }

    private fun handleUpdateUserResponse(response: Response<PatchUserResponse>): Resource<PatchUserResponse> {
        return if (response.isSuccessful) {
            response.body()?.let { Resource.Success(it) } ?: Resource.Error("Empty response")
        } else {
            Resource.Error(response.message())
        }
    }

    private fun handleChangePasswordResponse(response: Response<ChangePasswordResponse>): Resource<ChangePasswordResponse> {
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
