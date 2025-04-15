package com.itblaze.mentor.ui.screens.search

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.itblaze.mentor.R
import com.itblaze.mentor.data.db.MainDB
import com.itblaze.mentor.data.models.api.requests.requests.post.PostRequestsRequest
import com.itblaze.mentor.data.models.api.requests.tag.get.TagsResponse
import com.itblaze.mentor.data.models.api.requests.user.login.LoginUserRequest
import com.itblaze.mentor.data.models.api.requests.user.login.LoginUserResponse
import com.itblaze.mentor.data.repository.RequestRepository
import com.itblaze.mentor.data.repository.UserRepository
import com.itblaze.mentor.data.util.Resource
import com.itblaze.mentor.data.view.model.RequestViewModel
import com.itblaze.mentor.data.view.model.RequestViewModelProviderFactory
import com.itblaze.mentor.data.view.model.UserViewModel
import com.itblaze.mentor.data.view.model.UserViewModelProviderFactory

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FoundedProfile(
    navigationController: NavHostController,
    paddingValues: PaddingValues,
    login: String,
    text: String,
    tags: List<TagsResponse>,
    application: Application,
    database: MainDB
) {

    val viewModelUser: UserViewModel = viewModel(
        factory = UserViewModelProviderFactory(
            application = application,
            repository = UserRepository()
        )
    )

    val mentor by viewModelUser.loginUserData.observeAsState(null)

    val viewModelRequest: RequestViewModel = viewModel(
        factory = RequestViewModelProviderFactory(application, RequestRepository())
    )


//    val viewModel: DBRequestsViewModel = viewModel(
//        factory = DBRequestsViewModelProviderFactory(database)
//    )

    LaunchedEffect(Unit) {
        viewModelUser.getUserByLogin(
            LoginUserRequest(
                login
            )
        )
    }

//    val profile = MentorProfile(
//        id = 12,
//        fio = "qweqweqwe",
//        description = "Prod",
//        course = 1,
//        login = "boba",
//        tg = "qlldksd",
//        role = Role.Mentor.role,
//        tag = listOf("ht", "ht", "sef"),
//        rating = 4.9
//    )

    Box(
        Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {

        when (mentor) {
            is Resource.Error -> {
                val errorMessage = (mentor as Resource.Error).exception
                Text(stringResource(R.string.error) + errorMessage)
            }

            is Resource.Loading -> {
                Text(stringResource(R.string.loading))
            }

            null -> {
                Text(stringResource(R.string.null_value))
            }

            is Resource.Success<*> -> {
                val mentorData =
                    (mentor as Resource.Success<LoginUserResponse>).data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Карточка профиля
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Фотография профиля
                            AsyncImage(
                                model = mentorData.profileImage
                                    ?: R.drawable.it_blaze_profile, // Заглушка, если фото отсутствует
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // ФИО
                            Text(
                                text = mentorData.fio,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )

                            // Логин
                            Text(
                                text = "@${mentorData.login}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )

                            // Telegram
                            Text(
                                text = "Telegram: ${mentorData.tg}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )

                            // Описание (если есть)
                            mentorData.description.let {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }

                            // Курс (если есть)
                            mentorData.course.let {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Курс: $it",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Теги
//                            FlowRow(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.spacedBy(8.dp)
//                            ) {
//                                mentorData.tags.forEach { tag ->
//                                    Box() {
//                                        Text(text = tag)
//                                    }
//                                }
//                            }
                        }
                    }

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tags.forEach { tag ->
                            Box() {
                                Text(text = tag.name)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text)

                    // Дополнительные действия (например, кнопка "Написать сообщение")
                    Button(
                        onClick = {
                            viewModelRequest.createRequest(PostRequestsRequest(
                                mentor = mentorData.id,
                                tags = tags.map { it.id },
                                problem = text
                            ))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Отправить заявку")
                    }
                }
            }
        }
    }
}