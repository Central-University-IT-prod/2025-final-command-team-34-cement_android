package com.itblaze.mentor.ui.screens.authorization

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.itblaze.mentor.R
import com.itblaze.mentor.data.util.Role
import com.itblaze.mentor.ui.screens.Screens
import com.itblaze.mentor.ui.theme.DarkBlue

@Composable
fun SignupChooseUI(paddingValues: PaddingValues, navigationController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(
//                    listOf(
//                        MaterialTheme.colorScheme.background,
//                        DarkBlue
//                    )
//                )
//            )
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = {
                navigationController.navigate(Screens.Signup.screen + "/" + Role.Mentor.role) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier.padding(8.dp).weight(1f)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painterResource(R.drawable.it_blaze_mentor),
                    null
                )
                Text(stringResource(R.string.mentor), style = MaterialTheme.typography.bodyLarge)
            }
        }
        TextButton(
            onClick = {
                navigationController.navigate(Screens.Signup.screen + "/" + Role.Student.role) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier.padding(8.dp).weight(1f)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painterResource(R.drawable.it_blaze_student),
                    null
                )
                Text(stringResource(R.string.student), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}