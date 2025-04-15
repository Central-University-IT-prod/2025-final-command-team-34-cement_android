package com.itblaze.mentor.ui.screens.requests

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RatingBar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.itblaze.mentor.R
import com.itblaze.mentor.data.db.MainDB
import com.itblaze.mentor.data.models.api.requests.requests.accept.AcceptRequestRequest
import com.itblaze.mentor.data.models.api.requests.requests.decline.DeclineRequestRequest
import com.itblaze.mentor.data.models.api.requests.requests.id.GetIdRequestRequest
import com.itblaze.mentor.data.models.api.requests.requests.id.GetIdRequestResponse
import com.itblaze.mentor.data.repository.MentorRepository
import com.itblaze.mentor.data.repository.RequestRepository
import com.itblaze.mentor.data.util.Resource
import com.itblaze.mentor.data.util.Role
import com.itblaze.mentor.data.util.Status
import com.itblaze.mentor.data.view.model.MentorViewModelProviderFactory
import com.itblaze.mentor.data.view.model.MentorsViewModel
import com.itblaze.mentor.data.view.model.RequestViewModel
import com.itblaze.mentor.data.view.model.RequestViewModelProviderFactory

@Composable
fun RequestDetailUI(
    paddingValues: PaddingValues,
    requestId: Int?,
    navigationController: NavHostController,
    database: MainDB,
    role: String?,
    application: Application
) {
    val viewModel: RequestViewModel = viewModel(
        factory = RequestViewModelProviderFactory(
            application,
            RequestRepository()
        )
    )

    val viewModelRate: MentorsViewModel = viewModel(
        factory = MentorViewModelProviderFactory(
            application,
            MentorRepository()
        )
    )

    val requestDetails by viewModel.requestDetailsData.observeAsState()

    if (requestId == null) {
        Text("Ошибка: Неверный ID запроса")
    } else {
        LaunchedEffect(requestId) {
            viewModel.getRequestById(GetIdRequestRequest(requestId))
        }

        Box(
            Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            contentAlignment = Alignment.Center
        ) {
            when (requestDetails) {
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }

                is Resource.Success -> {
                    val request = (requestDetails as Resource.Success<GetIdRequestResponse>).data
                    if (role == Role.Mentor.role) {
                        MentorDetailView(request, viewModel)
                    } else {
                        StudentDetailView(request, viewModelRate)
                    }
                }

                is Resource.Error -> {
                    Text("Ошибка загрузки данных: ${(requestDetails as Resource.Error).exception}")
                }

                null -> {}
            }
        }
    }
}


@Composable
fun MentorDetailView(request: GetIdRequestResponse, viewModel: RequestViewModel) {
    Column {
        Text(text = "Информация о заявке", style = MaterialTheme.typography.titleLarge)
        // Здесь отображаем детали заявки
        Text(text = "Ментор: ${request.student.fio}")
        when(request.status) {
            Status.InProgress.status -> {
                Text("Статус:" + stringResource(Status.InProgress.statusRes))
            }
            Status.Accepted.status -> {
                Text("Статус:" + stringResource(Status.Accepted.statusRes))
            }
            Status.Declined.status -> {
                Text("Статус:" + stringResource(Status.Declined.statusRes))
            }
        }

        Text("Текст проблемы: " + request.problem)

        if(request.status == Status.InProgress.status) {
            Row {
                Button(onClick = { viewModel.acceptRequest(AcceptRequestRequest(request.id.toInt())) }) {
                    Text("Принять")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { viewModel.declineRequest(DeclineRequestRequest(request.id.toInt())) }) {
                    Text("Отклонить")
                }
            }
        }

        val context = LocalContext.current
        val telegramUsername = request.student.tg // Предполагается, что это имя пользователя Telegram
        val message = "Ваше сообщение здесь" // Сообщение, которое вы хотите отправить

        // Отображаем тег, если статус принят
        if (request.status == Status.Accepted.status) {
            Text(
                text = "Telegram: $telegramUsername",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    openTelegram(context, telegramUsername, message)
                }
            )
        }
    }
}

private fun openTelegram(context: Context, username: String, message: String) {
    val url = "https://t.me/$username?text=${Uri.encode(message)}"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

@Composable
fun StudentDetailView(request: GetIdRequestResponse, viewModel: MentorsViewModel) {
    var isRated by remember { mutableStateOf(false) }
    var rating by remember { mutableStateOf(0) }

    Column {
        Text(text = "Информация о заявке", style = MaterialTheme.typography.titleLarge)
        Text(text = "Ментор: ${request.mentor.fio}")

        when (request.status) {
            Status.InProgress.status -> {
                Text("Статус: " + stringResource(Status.InProgress.statusRes))
            }
            Status.Accepted.status -> {
                Text("Статус: " + stringResource(Status.Accepted.statusRes))
            }
            Status.Declined.status -> {
                Text("Статус: " + stringResource(Status.Declined.statusRes))
            }
        }
        Text("Текст проблемы: " + request.problem)

        // Отображаем тег телеграмма, если статус принят
        if (request.status == Status.Accepted.status) {
            Text(text = "Telegram: ${request.mentor.tg}")
            Spacer(modifier = Modifier.height(10.dp))

            if (!isRated) {
                RatingBar(
                    rating = rating,
                    onRatingChanged = { newRating ->
                        rating = newRating
                    },
                    onSubmit = {
                        viewModel.rateMentor(request.mentor.login, rating)
                        isRated = true // Скрыть оценщик после голосования
                    }
                )
            } else {
                val context = LocalContext.current
                val telegramUsername = request.mentor.tg // Предполагается, что это имя пользователя Telegram
                val message = "Ваше сообщение здесь" // Сообщение, которое вы хотите отправить

                // Отображаем тег, если статус принят
                if (request.status == Status.Accepted.status) {
                    Text(
                        text = "Telegram: $telegramUsername",
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            openTelegram(context, telegramUsername, message)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RatingBar(rating: Int, onRatingChanged: (Int) -> Unit, onSubmit: () -> Unit) {
    Row {
        for (i in 1..5) {
            val starColor = if (i <= rating) Color.Yellow else Color.Gray
            Icon(
                painterResource(R.drawable.it_blaze_croissant),
                contentDescription = "Croissant $i",
                tint = starColor,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onRatingChanged(i)
                    }
            )
        }
        Button(onClick = onSubmit, modifier = Modifier.padding(start = 8.dp)) {
            Text("Оценить")
        }
    }
}


