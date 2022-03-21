package com.mes.topdawg.android.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mes.topdawg.android.ui.theme.darkenColor
import com.mes.topdawg.android.ui.theme.getRandomLightColor
import com.mes.topdawg.android.ui.theme.orange200
import com.mes.topdawg.common.entity.local.DogBreed
import org.koin.androidx.compose.getViewModel

const val HomeTag = "Home"

@Composable
@ExperimentalCoilApi
fun HomeScreen(
    paddingValues: PaddingValues = PaddingValues(),
    dogBreedSelected: (dogBreed: DogBreed) -> Unit,
    homeScreenViewModel: HomeScreenViewModel = getViewModel()
) {
    val randomDogBreedState = homeScreenViewModel.randomDogBreed.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🦴 Top Dawg 🐩",
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
        }
    ) {
        Column(
            modifier = Modifier
                .testTag(HomeTag)
        ) {
            randomDogBreedState.value?.let {
                RandomDogBreedView(
                    dogBreed = it,
                    onSelected = dogBreedSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun RandomDogBreedView(
    modifier: Modifier = Modifier,
    dogBreed: DogBreed,
    onSelected: (dogBreed: DogBreed) -> Unit,
    contentColor: Color = getRandomLightColor()
) {

    Column(
        modifier = modifier
//            .clip(RoundedCornerShape(12.dp))
            .background(color = contentColor)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = { onSelected(dogBreed) }),
    ) {

        if (dogBreed.imageUrl.isNotEmpty()) {
            Image(
                painter = rememberImagePainter(dogBreed.imageUrl),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentDescription = dogBreed.name,
                contentScale = ContentScale.FillWidth
            )
        } else {
            Spacer(modifier = Modifier.size(60.dp))
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(contentColor)
                .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(text = dogBreed.name, style = TextStyle(fontSize = 20.sp))
            Text(
                text = dogBreed.bredFor,
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                text = dogBreed.breedGroup,
                style = TextStyle(color = contentColor.darkenColor(), fontSize = 14.sp)
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun DogBreedGroupItemView(dogBreed: DogBreed, onSelected: (dogBreed: DogBreed) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onSelected(dogBreed) })
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (dogBreed.imageUrl.isNotEmpty()) {
            Image(
                painter = rememberImagePainter(dogBreed.imageUrl),
                modifier = Modifier.size(60.dp), contentDescription = dogBreed.name
            )
        } else {
            Spacer(modifier = Modifier.size(60.dp))
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column {
            Text(text = dogBreed.name, style = TextStyle(fontSize = 20.sp))
            Text(
                text = dogBreed.breedGroup,
                style = TextStyle(color = Color.DarkGray, fontSize = 14.sp)
            )
        }
    }
}


