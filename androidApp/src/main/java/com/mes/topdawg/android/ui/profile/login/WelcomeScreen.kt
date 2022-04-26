package com.mes.topdawg.android.ui.profile.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
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
    val uiState = viewModel.profileState.collectAsState()
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
                    CircularProgressIndicator()
                    Text(text = "LoggingIn", modifier = Modifier)
                }
                is UserProfileState.LoginSuccess -> {
                    Text(text = "LoginSuccess", modifier = Modifier, color = Color.Green)
                }
                is UserProfileState.LoginFailed -> {
                    Text(text = "LoginFailed", modifier = Modifier, color = Color.Red)
                }
                is UserProfileState.LoggedIn -> {
                    Text(text = "LoggedIn", modifier = Modifier)
                }
                is UserProfileState.LoggingOut -> {
                    CircularProgressIndicator()
                    Text(text = "LoggingOut", modifier = Modifier)
                }
                is UserProfileState.LoggedOut -> {
                    Text(text = "LoggedOut", modifier = Modifier)
                }
            }
            Button(onClick = { /*TODO*/ }, ) {
                Text(text = "")
            }
        }
    }

}