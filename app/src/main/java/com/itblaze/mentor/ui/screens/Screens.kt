package com.itblaze.mentor.ui.screens

import com.itblaze.mentor.R
import com.itblaze.mentor.data.util.Constants.Companion.currentRole
import com.itblaze.mentor.data.util.Role

sealed class Screens(
    val titleRes: Int,
    val screen: String,
    val iconRes: Int? = null,
    val showIconOnBottomBar: Boolean = true,
    val showTopBar: Boolean = true,
    val showBottomBar: Boolean = true,
    val showActionButton: Boolean = false
) {
    // Authorization
    data object Signup : Screens(
        titleRes = R.string.signup,
        screen = "signup",
        showTopBar = false,
        showBottomBar = false,
        showIconOnBottomBar = false
    )

    data object SignupChoose : Screens(
        titleRes = R.string.signup,
        screen = "signup_choose",
        showTopBar = false,
        showBottomBar = false,
        showIconOnBottomBar = false
    )

    data object Login : Screens(
        titleRes = R.string.login,
        screen = "login",
        showTopBar = false,
        showBottomBar = false,
        showIconOnBottomBar = false
    )

    // Search
    data object Search : Screens(
        titleRes = R.string.search,
        screen = "search_main",
        iconRes = R.drawable.it_blaze_search,
        showIconOnBottomBar = if(currentRole == Role.Student.role) true else false
    )

    data object SearchRequest : Screens(
        titleRes = R.string.search,
        screen = "search_request",
        showTopBar = false,
        showBottomBar = false,
        showIconOnBottomBar = false
    )

    data object FoundedProfile : Screens(
        titleRes = R.string.founded_profile,
        screen = "founded_profile",
        showTopBar = false,
        showBottomBar = false,
        showIconOnBottomBar = false
    )

    // Profiles
    data object RequestsList : Screens(
        titleRes = R.string.profiles_list,
        screen = "requests_list",
        iconRes = R.drawable.it_blaze_chat
    )

    data object RequestDetail : Screens(
        titleRes = R.string.profile,
        screen = "request",
        showTopBar = false,
        showBottomBar = false,
        showIconOnBottomBar = false
    )

    // MyProfile
    data object MyProfile : Screens(
        titleRes = R.string.profile,
        screen = "my_profile",
        iconRes = R.drawable.it_blaze_profile
    )
}