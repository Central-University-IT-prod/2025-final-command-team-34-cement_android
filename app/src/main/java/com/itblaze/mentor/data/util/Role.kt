package com.itblaze.mentor.data.util

import androidx.compose.ui.graphics.vector.ImageVector
import com.itblaze.mentor.R
import com.itblaze.mentor.data.util.Constants.Companion.ROLE_MENTOR
import com.itblaze.mentor.data.util.Constants.Companion.ROLE_STUDENT
import com.itblaze.mentor.data.util.Constants.Companion.ROLE_SUPERUSER
import com.itblaze.mentor.ui.screens.Screens

sealed class Role(
    val role: String,
    val roleRes: Int
) {
    data object Admin : Role(
        ROLE_SUPERUSER,
        R.string.admin
    )

    data object Mentor : Role(
        ROLE_MENTOR,
        R.string.mentor
    )

    data object Student : Role(
        ROLE_STUDENT,
        R.string.student
    )
}