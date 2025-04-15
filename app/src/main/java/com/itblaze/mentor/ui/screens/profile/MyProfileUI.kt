package com.itblaze.mentor.ui.screens.profile

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.itblaze.mentor.R
import com.itblaze.mentor.data.models.api.requests.user.get.GetUser
import com.itblaze.mentor.data.models.api.requests.user.patch.PatchUserRequest
import com.itblaze.mentor.data.repository.UserRepository
import com.itblaze.mentor.data.util.Resource
import com.itblaze.mentor.data.view.model.UserViewModel
import com.itblaze.mentor.data.view.model.UserViewModelProviderFactory

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MyProfile(paddingValues: PaddingValues, application: Application) {
    val viewModel: UserViewModel = viewModel(
        factory = UserViewModelProviderFactory(
            application,
            UserRepository()
        )
    )

    val userData by viewModel.userData.observeAsState(null)

    // Состояние для редактируемых полей
    val (fio, setFio) = remember { mutableStateOf("") }
    val (login, setLogin) = remember { mutableStateOf("") }
    val (tg, setTg) = remember { mutableStateOf("") }
    val (description, setDescription) = remember { mutableStateOf("") }
    val (course, setCourse) = remember { mutableStateOf("") }
    val (isEditing, setIsEditing) = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }

    when (userData) {
        is Resource.Loading -> {
            CircularProgressIndicator()
        }

        is Resource.Success<*> -> {
            val user = (userData as Resource.Success<GetUser>).data
            if (!isEditing) {
                // Устанавливаем значения из userData
                setFio(user.fio)
                setLogin(user.login)
                setTg(user.tg)
                setDescription(user.description)
                setCourse(user.course.toString())
            }

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Фотография профиля
                    AsyncImage(
                        model = user.profileImage
                            ?: R.drawable.it_blaze_profile, // Заглушка, если фото отсутствует
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    // ФИО
                    if (isEditing) {
                        TextField(
                            value = fio,
                            onValueChange = setFio,
                            label = { Text("Фамилия Имя Отчество") }
                        )
                    } else {
                        Text(text = fio, style = MaterialTheme.typography.bodyLarge)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Логин
                    if (isEditing) {
                        TextField(
                            value = login,
                            onValueChange = setLogin,
                            label = { Text("Логин") }
                        )
                    } else {
                        Text(text = "@$login", style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Telegram
                    if (isEditing) {
                        TextField(
                            value = tg,
                            onValueChange = setTg,
                            label = { Text("Telegram") }
                        )
                    } else {
                        Text(text = "Telegram: $tg", style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Описание
                    if (isEditing) {
                        TextField(
                            value = description,
                            onValueChange = setDescription,
                            label = { Text("Описание") },
                            modifier = Modifier.height(100.dp)
                        )
                    } else {
                        Text(text = description, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Курс
                    if (isEditing) {
                        TextField(
                            value = course,
                            onValueChange = setCourse,
                            label = { Text("Курс") }
                        )
                    } else {
                        Text(text = "Курс: $course", style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Кнопка редактирования или сохранения
                    Button(
                        onClick = {
                            if (isEditing) {
                                // Сохранение данных на сервер
                                val updatedUser = PatchUserRequest(
                                    fio = fio,
                                    login = login,
                                    tg = tg,
                                    description = description,
                                    course = course.toLong()
                                )
                                viewModel.updateUser(updatedUser)
                                viewModel.getCurrentUser()
                                setIsEditing(false)
                            } else {
                                setIsEditing(true)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF6A11CB)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isEditing) "Сохранить" else "Редактировать",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        is Resource.Error -> {
//            Text(text = "Ошибка загрузки данных")
        }

        null -> {}
    }
}
