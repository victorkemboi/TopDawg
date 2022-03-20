package com.mes.topdawg.android.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.mes.topdawg.common.entity.local.DogBreed
import org.koin.androidx.compose.getViewModel

const val HomeTag = "Home"

@Composable
fun HomeScreen(
    paddingValues: PaddingValues = PaddingValues(),
    breedSelected: (dogBreed: DogBreed) -> Unit,
    homeScreenViewModel: HomeScreenViewModel = getViewModel()
) {
    val randomBreedState = homeScreenViewModel.randomBreed.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("🦴 Top Dawg 🐩") })
        }
    ) {
        Column(
            modifier = Modifier
                .testTag(HomeTag)
                .padding(paddingValues = paddingValues)
        ) {

        }
    }
}