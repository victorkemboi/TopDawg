package com.mes.topdawg.android.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import co.touchlab.kermit.Logger
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
@ExperimentalComposeUiApi
fun HomeScreen(
    paddingValues: PaddingValues = PaddingValues(),
    dogBreedSelected: (dogBreed: DogBreed) -> Unit,
    homeScreenViewModel: HomeScreenViewModel = getViewModel()
) {
    val logger = Logger.withTag("HomeScreen")
    val randomDogBreedState = homeScreenViewModel.randomDogBreed.collectAsState()

    LaunchedEffect(key1 = Unit) {
        homeScreenViewModel.fetchRandomDogBreed()
    }

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
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (randomDogBreed, searchView) = createRefs()
            Column(
                modifier = Modifier
                    .testTag(HomeTag)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .constrainAs(randomDogBreed) {
                        top.linkTo(parent.top)
                        
                    }
            ) {
                val breed = randomDogBreedState.value
                if (breed != null) {
                    logger.i { "The breed state is not empty: $breed" }
                    RandomDogBreedView(
                        dogBreed = breed,
                        onSelected = dogBreedSelected,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(bottom = 8.dp)
                    )
                } else {
                    logger.i { "The breed state is empty." }
                    SideEffect {
                        homeScreenViewModel.fetchRandomDogBreed()
                    }
                }
            }

            SearchView(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 16.dp, bottom = paddingValues.calculateBottomPadding())
                    .constrainAs(searchView) {
                        bottom.linkTo(parent.bottom)
                    },
                searchText = homeScreenViewModel.searchQueryState.value,
                onSearchTextChanged = {
                    homeScreenViewModel.searchQueryState.value = it
                },
                onClearClick = {
                    homeScreenViewModel.searchQueryState.value = ""
                },
            )

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

@ExperimentalComposeUiApi
@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    searchText: String = "",
    placeholderText: String = "Search...",
    onSearchTextChanged: (String) -> Unit = {},
    onClearClick: () -> Unit = {},
) {

    var showClearButton by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .onFocusChanged { focusState ->
                showClearButton = (focusState.isFocused)
            }
            .focusRequester(focusRequester),
        value = searchText,
        onValueChange = onSearchTextChanged,
        placeholder = {
            Text(text = placeholderText)
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            cursorColor = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
        ),
        trailingIcon = {
            AnimatedVisibility(
                visible = showClearButton,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = {
                    if (searchText.isEmpty()) {
                        focusManager.clearFocus()
                    } else {
                        onClearClick()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close icon"
                    )
                }

            }
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        }),
    )

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


