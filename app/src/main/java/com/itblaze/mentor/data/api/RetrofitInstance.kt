package com.itblaze.mentor.data.api

import com.itblaze.mentor.data.util.Constants.Companion.BASE_URL
import com.itblaze.mentor.data.util.Constants.Companion.tokenUser
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {

        private val retrofitAuthorization by lazy {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Логирование запросов и ответов
            }

            // Создаем Interceptor для добавления токена в заголовок
            val authInterceptor = Interceptor { chain ->
                val originalRequest = chain.request()
                val requestWithToken = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $tokenUser") // Добавляем токен
                    .build()
                chain.proceed(requestWithToken)
            }

            // Настраиваем OkHttpClient с Interceptor'ами
            val client = OkHttpClient.Builder()
                .addInterceptor(logging) // Логирование
                .addInterceptor(authInterceptor) // Добавление токена
                .build()

            // Создаем Retrofit
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val authorizationAPI by lazy {
            retrofitAuthorization.create(AuthorizationAPI::class.java)
        }

        private val retrofitMentors by lazy {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Логирование запросов и ответов
            }

            // Создаем Interceptor для добавления токена в заголовок
            val authInterceptor = Interceptor { chain ->
                val originalRequest = chain.request()
                val requestWithToken = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $tokenUser") // Добавляем токен
                    .build()
                chain.proceed(requestWithToken)
            }

            // Настраиваем OkHttpClient с Interceptor'ами
            val client = OkHttpClient.Builder()
                .addInterceptor(logging) // Логирование
                .addInterceptor(authInterceptor) // Добавление токена
                .build()

            // Создаем Retrofit
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val mentorsAPI by lazy {
            retrofitMentors.create(MentorsAPI::class.java)
        }

        private val retrofitRequests by lazy {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Логирование запросов и ответов
            }

            val authInterceptor = Interceptor { chain ->
                val originalRequest = chain.request()
                val requestWithToken = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $tokenUser") // Добавляем токен
                    .build()
                chain.proceed(requestWithToken)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging) // Логирование
                .addInterceptor(authInterceptor) // Добавление токена
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val requestAPI by lazy {
            retrofitRequests.create(RequestAPI::class.java)
        }

        private val retrofitSearch by lazy {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Логирование запросов и ответов
            }

            val authInterceptor = Interceptor { chain ->
                val originalRequest = chain.request()
                val requestWithToken = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $tokenUser") // Добавляем токен
                    .build()
                chain.proceed(requestWithToken)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging) // Логирование
                .addInterceptor(authInterceptor) // Добавление токена
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val searchAPI by lazy {
            retrofitSearch.create(SearchAPI::class.java)
        }

        private val retrofitTags by lazy {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Логирование запросов и ответов
            }

            val authInterceptor = Interceptor { chain ->
                val originalRequest = chain.request()
                val requestWithToken = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $tokenUser") // Добавляем токен
                    .build()
                chain.proceed(requestWithToken)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging) // Логирование
                .addInterceptor(authInterceptor) // Добавление токена
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val tagAPI by lazy {
            retrofitTags.create(TagAPI::class.java)
        }

        private val retrofitUsers by lazy {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Логирование запросов и ответов
            }

            val authInterceptor = Interceptor { chain ->
                val originalRequest = chain.request()
                val requestWithToken = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $tokenUser") // Добавляем токен
                    .build()
                chain.proceed(requestWithToken)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging) // Логирование
                .addInterceptor(authInterceptor) // Добавление токена
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val userAPI by lazy {
            retrofitUsers.create(UserAPI::class.java)
        }
    }
}