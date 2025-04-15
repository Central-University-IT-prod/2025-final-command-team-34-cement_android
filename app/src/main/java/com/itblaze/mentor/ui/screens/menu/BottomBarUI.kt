package com.itblaze.mentor.ui.screens.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.itblaze.mentor.data.util.Constants.Companion.currentRole
import com.itblaze.mentor.data.util.Role
import com.itblaze.mentor.ui.screens.Screens
import com.itblaze.mentor.ui.theme.MainBlue

@Composable
fun BottomBarUI(
    navigationController: NavHostController,
    screenItemsBar: List<Screens>,
    bottomBarState: MutableState<Boolean>
) {
    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomAppBar(containerColor = MainBlue) {
                screenItemsBar.forEach { screens ->
                    if (screens.showIconOnBottomBar && screens.iconRes != null) {
//                        if (
//                            (currentDestination?.route == Screens.Search.screen && currentRole == Role.Student.role) ||
//                            (currentDestination?.route == Screens.RequestsList.screen && currentRole == Role.Mentor.role) ||
//                            (currentDestination?.route == Screens.MyProfile.screen)
//                        ) {
                            NavigationBarItem(
                                selected = false,
                                onClick = {
                                    navigationController.navigate(screens.screen) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        painterResource(screens.iconRes),
                                        stringResource(screens.titleRes),
                                        modifier = Modifier
                                            .size(if (currentDestination?.route == screens.screen) 40.dp else 30.dp)
                                    )
                                },
                                colors = NavigationBarItemColors(
                                    selectedIconColor = Color.Unspecified,
                                    selectedTextColor = Color.Unspecified,
                                    selectedIndicatorColor = Color.Unspecified,
                                    unselectedIconColor = Color.Unspecified,
                                    unselectedTextColor = Color.Unspecified,
                                    disabledIconColor = Color.Unspecified,
                                    disabledTextColor = Color.Unspecified
                                )
                            )
                        }
//                    }
                }
            }
        }
    )
}