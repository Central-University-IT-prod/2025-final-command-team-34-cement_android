package com.itblaze.mentor.ui.screens.requests

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.itblaze.mentor.R
import com.itblaze.mentor.data.models.api.requests.requests.accept.AcceptRequestRequest
import com.itblaze.mentor.data.models.api.requests.requests.decline.DeclineRequestRequest
import com.itblaze.mentor.data.models.api.requests.requests.get.GetRequestResponse
import com.itblaze.mentor.data.repository.RequestRepository
import com.itblaze.mentor.data.util.Resource
import com.itblaze.mentor.data.util.Role
import com.itblaze.mentor.data.util.Status
import com.itblaze.mentor.data.view.model.RequestViewModel
import com.itblaze.mentor.data.view.model.RequestViewModelProviderFactory
import com.itblaze.mentor.ui.screens.Screens

@Composable
fun RequestsListUI(
    navigationController: NavHostController,
    paddingValues: PaddingValues,
    role: String?,
    application: Application
) {

    val viewModel: RequestViewModel = viewModel(
        factory = RequestViewModelProviderFactory(
            application = application,
            repository = RequestRepository()
        )
    )

    val requestsState by viewModel.requestsData.observeAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (requestsState) {
            is Resource.Loading -> {
                CircularProgressIndicator()
            }

            is Resource.Success -> {
                val requests = (requestsState as Resource.Success<List<GetRequestResponse>>).data
                if (requests.isNotEmpty()) {
                    LazyColumn {
                        items(requests) { request ->
                            MentorRequestItem(request, navigationController, role, viewModel)
                        }
                    }
                } else {
                    Text("Нет данных")
                }
            }

            is Resource.Error -> {
                Text("Ошибка: ${(requestsState as Resource.Error).exception}")
            }

            null -> {}
        }
    }
}


@Composable
fun MentorRequestItem(
    requestItem: GetRequestResponse,
    navigationController: NavHostController,
    role: String?,
    viewModel: RequestViewModel
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                navigationController.navigate(Screens.RequestDetail.screen + "/${requestItem.id}") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Изображение профиля
        Image(
            painter = painterResource(R.drawable.it_blaze_profile),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color.LightGray)
        )
        Spacer(modifier = Modifier.width(16.dp))

        // Информация о профиле
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (role == Role.Student.role) requestItem.student.fio else requestItem.mentor.fio,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Статус
            when(requestItem.status) {
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
        }
    }
}

