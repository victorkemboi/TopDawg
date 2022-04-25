package com.mes.topdawg.android.ui.profile.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mes.topdawg.android.ui.Screen
import com.mes.topdawg.android.ui.theme.orange200
import com.mes.topdawg.common.state.UserProfileState
import org.koin.androidx.compose.getViewModel

@Composable
fun WelcomeScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navController: NavController,
    viewModel: WelcomeScreenViewModel = getViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = Screen.Welcome.title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = orange200
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp),
            backgroundColor = Color.Black
        )
    }) {
        Column(modifier = Modifier.padding(it)) {
            when (uiState.value) {
                is UserProfileState.LoggingIn -> {

                }
                is UserProfileState.LoginSuccess -> {

                }
                is UserProfileState.LoginFailed -> {

                }
                is UserProfileState.LoggedIn -> {

                }
                is UserProfileState.LoggingOut -> {

                }
                is UserProfileState.LoggedOut -> {

                }
            }
        }
    }

}