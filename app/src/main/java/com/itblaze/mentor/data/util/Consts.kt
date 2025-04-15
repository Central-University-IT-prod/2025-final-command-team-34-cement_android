package com.itblaze.mentor.data.util

class Constants {
    companion object {
        const val BASE_URL = "http://prod-team-34-rqpd9aim.REDACTED/api/"
        const val BASE_URL_WEBSOCKET = "ws://localhost:10000/ws/chat/"
        var tokenUser: String? = null
        var currentRole: String? = Role.Student.role

        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val UNABLE_TO_CONNECT = "Unable to connect"
        const val NO_SIGNAL = "No signal"
        const val TOO_MANY_REQUESTS = "Too Many Requests"
        const val INVALID_SERVER_REQUESTS = "Invalid server requests"

        const val ROLE_SUPERUSER = "admin"
        const val ROLE_MENTOR = "mentor"
        const val ROLE_STUDENT = "student"

        const val STATUS_ACCEPTED = "accepted"
        const val STATUS_IN_PROCESS = "in_process"
        const val STATUS_DECLINED = "declined"
    }
}