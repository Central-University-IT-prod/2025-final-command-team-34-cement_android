package com.itblaze.mentor.ui.screens.search

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.itblaze.mentor.MainActivity
import com.itblaze.mentor.R
import com.itblaze.mentor.data.repository.AuthorizationRepository
import com.itblaze.mentor.data.view.model.AuthorizationViewModel
import com.itblaze.mentor.data.view.model.AuthorizationViewModelProviderFactory
import com.itblaze.mentor.ui.screens.Screens

@Composable
fun SearchUI(
    paddingValues: PaddingValues,
    navigationController: NavHostController
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.it_bleze_search),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Button(
            onClick = {
                navigationController.navigate(Screens.SearchRequest.screen) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        ) {
            Text(text = stringResource(R.string.find_mentor))
        }
    }
}