package com.itblaze.mentor.ui.screens.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.itblaze.mentor.R
import com.itblaze.mentor.ui.theme.MainBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarUI(
    topBarState: MutableState<Boolean>,
) {
    AnimatedVisibility(
        visible = topBarState.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        content = {
            TopAppBar(
                title = {
                    Row {
                        Icon(
                            painter = painterResource(R.drawable.it_blaze_logo),
                            stringResource(R.string.app_name),
                            modifier = Modifier.size(40.dp)
                        )
                        Text(stringResource(R.string.app_name))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MainBlue,
                    titleContentColor = Color.Unspecified,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.Unspecified
                )
            )
        }
    )
}