package com.itblaze.mentor.ui.screens.authorization

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.itblaze.mentor.R
import com.itblaze.mentor.data.models.api.requests.authorization.signup.mentor.SignupMentorResponse
import com.itblaze.mentor.data.models.api.requests.authorization.signup.student.SignupStudentResponse
import com.itblaze.mentor.data.repository.AuthorizationRepository
import com.itblaze.mentor.data.util.Constants.Companion.currentRole
import com.itblaze.mentor.data.util.Constants.Companion.tokenUser
import com.itblaze.mentor.data.util.Resource
import com.itblaze.mentor.data.util.Role
import com.itblaze.mentor.data.view.model.AuthorizationViewModel
import com.itblaze.mentor.data.view.model.AuthorizationViewModelProviderFactory
import com.itblaze.mentor.ui.screens.Screens
import com.itblaze.mentor.ui.theme.DarkBlue

@Composable
fun SignupUI(
    paddingValues: PaddingValues,
    navigationController: NavHostController,
    application: Application,
    role: String
) {

    val viewModel: AuthorizationViewModel = viewModel(
        factory = AuthorizationViewModelProviderFactory(
            application,
            AuthorizationRepository()
        )
    )

    val signupDataMentor by viewModel.signupMentorData.observeAsState(null)

    val signupDataStudent by viewModel.signupStudentData.observeAsState(null)

    var loginState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }
    var tg by remember { mutableStateOf("") }
    var descriptionState by remember { mutableStateOf("") }


    Box(
        Modifier
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
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.signup),  style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = loginState,
                onValueChange = {
                    loginState = it
                },
                label = {
                    Text(stringResource(R.string.login_name))
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = passwordState,
                onValueChange = {
                    passwordState = it
                },
                label = {
                    Text(text = stringResource(R.string.password))
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = tg,
                onValueChange = {
                    tg = it
                },
                label = {
                    Text(text = stringResource(R.string.telegram_name))
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = descriptionState,
                onValueChange = {
                    descriptionState = it
                },
                label = {
                    Text(text = stringResource(R.string.description))
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (role == Role.Student.role) {
                        viewModel.signupStudent(
                            login = loginState,
                            password = passwordState,
                            tg = tg,
                            fio = loginState,
                            curse = 1,
                            description = descriptionState
                        )
                    } else {
                        viewModel.signupMentor(
                            login = loginState,
                            password = passwordState,
                            tg = tg,
                            fio = loginState,
                            curse = 3,
                            description = descriptionState,
                            tag = listOf(1, 2),
                        )
                    }

                }
            ) {
                Text(text = stringResource(R.string.login))
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = {
                    navigationController.navigate(Screens.Login.screen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Text(text = stringResource(R.string.i_already_have_an_account_login))
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (role == Role.Student.role) {
                when (signupDataStudent) {
                    is Resource.Success<*> -> {
                        val userData =
                            (signupDataStudent as Resource.Success<SignupStudentResponse>).data
                        tokenUser = userData.token
                        currentRole = userData.role
                        navigationController.navigate(Screens.Search.screen) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }

                    is Resource.Error -> {
                        //val errorMessage = (signupDataStudent as Resource.Error).exception
                    }

                    is Resource.Loading -> {
                        CircularProgressIndicator()
                    }

                    null -> {
                    }
                }
            } else {
                when (signupDataMentor) {
                    is Resource.Success<*> -> {
                        val userData =
                            (signupDataMentor as Resource.Success<SignupMentorResponse>).data
                        tokenUser = userData.token
                        currentRole = userData.role
                        navigationController.navigate(Screens.RequestsList.screen) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }

                    is Resource.Error -> {
                        //val errorMessage = (signupDataMentor as Resource.Error).exception
                    }

                    is Resource.Loading -> {
                        CircularProgressIndicator()
                    }

                    null -> {
                    }
                }
            }
        }
    }
}
