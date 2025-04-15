package com.itblaze.mentor.ui.screens.menu

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.itblaze.mentor.MainActivity
import com.itblaze.mentor.data.db.MainDB
import com.itblaze.mentor.data.models.api.requests.tag.get.TagsResponse
import com.itblaze.mentor.data.util.Constants.Companion.currentRole
import com.itblaze.mentor.data.util.Role
import com.itblaze.mentor.ui.screens.Screens
import com.itblaze.mentor.ui.screens.authorization.LoginUI
import com.itblaze.mentor.ui.screens.authorization.SignupChooseUI
import com.itblaze.mentor.ui.screens.authorization.SignupUI
import com.itblaze.mentor.ui.screens.profile.MyProfile
import com.itblaze.mentor.ui.screens.requests.RequestDetailUI
import com.itblaze.mentor.ui.screens.requests.RequestsListUI
import com.itblaze.mentor.ui.screens.search.FoundedProfile
import com.itblaze.mentor.ui.screens.search.SearchRequestUI
import com.itblaze.mentor.ui.screens.search.SearchUI

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavMenuUI(
    database: MainDB,
    context: MainActivity,
    application: Application
) {

    val navigationController = rememberNavController()


    val screenItemsBar = if(currentRole == Role.Student.role) listOf(
        // Authorization
        Screens.Login,
        Screens.Signup,
        // Search
        Screens.Search, // Visible on bottom bar
        Screens.SearchRequest,
        Screens.FoundedProfile,
        // Profiles
        Screens.RequestsList, // Visible on bottom bar
        Screens.RequestDetail,
        // MyProfile
        Screens.MyProfile // Visible on bottom bar
    ) else {
        listOf(
            // Authorization
            Screens.Login,
            Screens.Signup,
            // Profiles
            Screens.RequestsList, // Visible on bottom bar
            Screens.RequestDetail,
            // MyProfile
            Screens.MyProfile // Visible on bottom bar
        )
    }

    val snackbarHostState = remember { SnackbarHostState() }

    // State of topBar, set state to false, if current page showTopBar = false
    val topBarState = rememberSaveable { (mutableStateOf(true)) }

    // State of bottomBar, set state to false, if current page showBottomBar = false
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    val actionButtonState = rememberSaveable { (mutableStateOf(true)) }


    // Some logic for hide bottom menu in some pages (Screens.SomePage.showBottomBar)
    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    screenItemsBar.forEach { screens ->

        // Find real address without transmitting data
        val route = navBackStackEntry?.destination?.route
        val index = route?.indexOf("/")

        val realRoute =
            if (index == -1) route else navBackStackEntry?.destination?.route?.substring(
                0, index!!
            )
        if (realRoute == screens.screen) {
            topBarState.value = screens.showTopBar
            bottomBarState.value = screens.showBottomBar
            actionButtonState.value = screens.showActionButton
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopBarUI(topBarState)
        },
        bottomBar = {
            BottomBarUI(navigationController, screenItemsBar, bottomBarState)
        }
    ) { paddingValues -> // padding of top bar and bottom bar
        NavHost(
            navController = navigationController, startDestination = Screens.Login.screen
        ) {
            // Authorization
            composable(Screens.Login.screen) {
                LoginUI(
                    paddingValues = paddingValues,
                    navigationController = navigationController,
                    context = context,
                    application = application
                )
            }

            composable(Screens.Signup.screen + "/{role}") {
                val role = it.arguments?.getString("role") ?: ""
                val roleClass =
                    if (role == Role.Mentor.role) Role.Mentor.role else Role.Student.role
                SignupUI(
                    paddingValues = paddingValues,
                    navigationController = navigationController,
                    application = application,
                    role = roleClass
                )
            }

            composable(Screens.SignupChoose.screen) {
                SignupChooseUI(
                    paddingValues = paddingValues,
                    navigationController = navigationController,
                )
            }

            // Search
            composable(Screens.Search.screen) {
                SearchUI(
                    paddingValues = paddingValues,
                    navigationController = navigationController
                )
            }

            composable(Screens.SearchRequest.screen) {
                SearchRequestUI(
                    paddingValues = paddingValues,
                    navigationController = navigationController,
                    application = application
                )
            }

            composable(Screens.FoundedProfile.screen + "/{login}/{text}/{tags}") { backStackEntry ->
                val login = backStackEntry.arguments?.getString("login") ?: ""
                val text = backStackEntry.arguments?.getString("text") ?: ""
                val tagsString = backStackEntry.arguments?.getString("tags") ?: ""

                // Десериализация строки обратно в список TagsResponse
                val tags = if (tagsString != "") tagsString.split(",").map {
                    val (id, name) = it.split(":")
                    TagsResponse(id.toInt(), name)
                } else listOf()

                FoundedProfile(
                    paddingValues = paddingValues,
                    navigationController = navigationController,
                    login = login,
                    text = text,
                    tags = tags,
                    application = application,
                    database = database
                )
            }

            // Profiles
            composable(Screens.RequestsList.screen) {
                RequestsListUI(
                    paddingValues = paddingValues,
                    navigationController = navigationController,
                    role = currentRole,
                    application = application
                )
            }

            composable(Screens.RequestDetail.screen + "/{id}") {
                val id = it.arguments?.getString("id")?.toIntOrNull()
                RequestDetailUI(
                    paddingValues = paddingValues,
                    requestId = id,
                    navigationController = navigationController,
                    database = database,
                    role = currentRole,
                    application = application
                )
            }

            // MyProfile
            composable(Screens.MyProfile.screen) {
                MyProfile(
                    paddingValues = paddingValues,
                    application = application
                )
            }
        }
    }
}