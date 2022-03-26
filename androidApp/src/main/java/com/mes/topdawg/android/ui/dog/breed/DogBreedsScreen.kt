package com.mes.topdawg.android.ui.dog.breed

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.mes.topdawg.android.ui.home.HomeScreenViewModel
import com.mes.topdawg.common.data.local.entity.DogBreed
import org.koin.androidx.compose.getViewModel

const val DogBreedsTag = "DogBreeds"

@Composable
fun DogBreedsScreen(
    paddingValues: PaddingValues = PaddingValues(),
    breedSelected: (dogBreed: DogBreed) -> Unit,
    homeScreenViewModel: HomeScreenViewModel = getViewModel()
) {
}
