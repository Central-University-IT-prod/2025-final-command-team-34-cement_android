package com.itblaze.mentor.ui.screens.search

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.itblaze.mentor.R
import com.itblaze.mentor.data.models.api.requests.search.SearchRequest
import com.itblaze.mentor.data.models.api.requests.search.SearchResponse
import com.itblaze.mentor.data.models.api.requests.tag.get.TagsResponse
import com.itblaze.mentor.data.repository.SearchRepository
import com.itblaze.mentor.data.repository.TagRepository
import com.itblaze.mentor.data.util.Resource
import com.itblaze.mentor.data.view.model.SearchViewModel
import com.itblaze.mentor.data.view.model.SearchViewModelProviderFactory
import com.itblaze.mentor.data.view.model.TagViewModel
import com.itblaze.mentor.data.view.model.TagViewModelProviderFactory
import com.itblaze.mentor.ui.screens.Screens
import com.itblaze.mentor.ui.theme.DarkBlue
import com.itblaze.mentor.ui.theme.LightBlue

@ExperimentalLayoutApi
@SuppressLint("MutableCollectionMutableState")
@Composable
fun SearchRequestUI(
    paddingValues: PaddingValues, navigationController: NavHostController, application: Application,
) {

    val viewModelTag: TagViewModel = viewModel(
        factory = TagViewModelProviderFactory(
            application = application,
            repository = TagRepository()
        )
    )

    val tagDataMentor by viewModelTag.tagsData.observeAsState(null)

    val viewModelSearch: SearchViewModel = viewModel(
        factory = SearchViewModelProviderFactory(
            application = application,
            repository = SearchRepository()
        )
    )

    LaunchedEffect(Unit) {
        viewModelTag.getTags()
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {
        LaunchedEffect(Unit) {
            viewModelSearch.searchMentors(
                SearchRequest(
                    tagId = emptyList(),
                    problem = null
                )
            )
        }

        when (tagDataMentor) {
            is Resource.Error -> {
                val errorMessage = (tagDataMentor as Resource.Error).exception
                Text(stringResource(R.string.error) + errorMessage)
            }

            is Resource.Loading -> {
                Text(stringResource(R.string.loading))
            }

            null -> {
                Text(stringResource(R.string.null_value))
            }

            is Resource.Success<*> -> {
                val tagData =
                    (tagDataMentor as Resource.Success<List<TagsResponse>>).data
//                Text("Signup successful!")

                val searchDataMentor by viewModelSearch.searchResults.observeAsState(null)

                val selectedTags = remember { mutableStateListOf<TagsResponse>() }
                var requestText by remember { mutableStateOf("") }

                var searchQuery by remember { mutableStateOf("") }
                var showSelectedOnly by remember { mutableStateOf(false) }

                // Фильтрация тегов по запросу
                val filteredTags = if (searchQuery.isNotEmpty()) {
                    tagData.filter { it.name.contains(searchQuery, ignoreCase = true) }
                } else {
                    tagData
                }

                // Отображаемые теги
                val displayedTags = if (showSelectedOnly) selectedTags else filteredTags



                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Column() {
                        // Поле поиска
                        // Анимация для поля поиска
                        AnimatedVisibility(
                            visible = !showSelectedOnly
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                label = { Text("Поиск тегов") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        AnimatedVisibility(
                            visible = showSelectedOnly
                        ) {
                            // Переключатель для отображения только выбранных тегов
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Показать только выбранные",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                Switch(
                                    checked = showSelectedOnly,
                                    onCheckedChange = { showSelectedOnly = it })
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Список тегов
                        FlowRow(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier.height( if(!showSelectedOnly) 150.dp else 50.dp)
                            ) {
                                LazyColumn {
                                    items(displayedTags) { tag ->
                                        val isSelected = selectedTags.contains(tag)
                                        TagItem(
                                            tag = tag.name,
                                            isSelected = isSelected,
                                            onTagClick = {
                                                if (isSelected) {
                                                    selectedTags.remove(tag)
                                                } else {
                                                    selectedTags.add(tag)
                                                }
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = requestText,
                                onValueChange = { requestText = it },
                                label = { Text("Введите просьбу здесь") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Button(modifier = Modifier
                                .padding(30.dp)
                                .fillMaxWidth(), onClick = {
                                showSelectedOnly = true
                                viewModelSearch.searchMentors(
                                    SearchRequest(
                                        tagId = selectedTags.map { it.id },
                                        problem = requestText.ifEmpty { null }
                                    )
                                )
                            }) {
                                Row {
                                    Text("Поиск")
                                }
                            }
                        }
                    }

                    when (searchDataMentor) {
                        is Resource.Error -> {
                            val errorMessage = (searchDataMentor as Resource.Error).exception
                            Text(stringResource(R.string.error) + errorMessage)
                        }

                        is Resource.Loading -> {
                            Text(stringResource(R.string.loading))
                        }

                        null -> {
                            Text(stringResource(R.string.null_value))
                        }

                        is Resource.Success<*> -> {
                            val mentorsData: List<SearchResponse> =
                                (searchDataMentor as Resource.Success<List<SearchResponse>>).data
                            LazyColumn {
                                items(mentorsData) { profile ->
                                    MentorProfileItem(
                                        profile,
                                        navigationController,
                                        selectedTags,
                                        requestText
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TagItem(tag: String, isSelected: Boolean, onTagClick: () -> Unit) {
    val backgroundColor =
        if (isSelected) DarkBlue else LightBlue

    Box(
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onTagClick)
            .clip(shape = RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            tag,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun MentorProfileItem(
    profile: SearchResponse,
    navigationController: NavHostController,
    displayedTags: SnapshotStateList<TagsResponse>,
    requestText: String
) {
    Column(modifier = Modifier
        .padding(16.dp)
        .clickable {
            val tagsString = displayedTags.joinToString(",") { "${it.id}:${it.name}" }
            navigationController.navigate(Screens.FoundedProfile.screen + "/${profile.login}/$requestText/$tagsString") {
                launchSingleTop = true
                restoreState = true
            }
        }) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            if (!profile.profileImage.isNullOrEmpty()) {
                AsyncImage(
                    model = profile.profileImage, // Ссылка на изображение
                    contentDescription = null, // Описание для accessibility
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.it_blaze_profile) // Стандартное изображение в случае ошибки
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.it_blaze_profile),
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile.fio,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Gray.copy(alpha = 0.2f))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.it_blaze_croissant),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = profile.mentorRating.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow {
                    items(profile.tags) { tag ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Gray.copy(alpha = 0.1f))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row {
                                Text(
                                    text = "#",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = DarkBlue
                                )
                                Text(
                                    text = tag.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        profile.description.let { ExpandableText(it, 3) }
    }

}
