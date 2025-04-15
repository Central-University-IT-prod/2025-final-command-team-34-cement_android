package com.itblaze.mentor.data.util

import com.itblaze.mentor.R
import com.itblaze.mentor.data.util.Constants.Companion.STATUS_ACCEPTED
import com.itblaze.mentor.data.util.Constants.Companion.STATUS_DECLINED
import com.itblaze.mentor.data.util.Constants.Companion.STATUS_IN_PROCESS

sealed class Status(
    val status: String,
    val statusRes: Int
) {
    data object Accepted : Status(
        STATUS_ACCEPTED,
        R.string.accepted
    )

    data object Declined : Status(
        STATUS_DECLINED,
        R.string.declined
    )

    data object InProgress : Status(
        STATUS_IN_PROCESS,
        R.string.in_progress
    )
}