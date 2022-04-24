package com.mes.topdawg.android.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.mes.topdawg.android.ui.theme.darkenColor
import com.mes.topdawg.android.ui.theme.getRandomLightColor
import com.mes.topdawg.android.ui.theme.orange200
import com.mes.topdawg.common.data.local.entity.DogBreed
import org.koin.androidx.compose.getViewModel

const val HomeTag = "Home"

@Composable
@ExperimentalCoilApi
@ExperimentalComposeUiApi
fun SearchScreen(
    paddingValues: PaddingValues = PaddingValues(),
    dogBreedSelected: (dogBreed: DogBreed) -> Unit,
    searchScreenViewModel: SearchScreenViewModel = getViewModel()
) {
    val logger = Logger.withTag("HomeScreen")
    val dogBreedSearchResultsState = searchScreenViewModel.dogBreedSearchResults.collectAsState()
    val searchQueryState = searchScreenViewModel.searchQueryState

    LaunchedEffect(key1 = Unit) {
        searchScreenViewModel.fetchRandomDogBreed()
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Top Dawg 🐩",
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
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                val dogBreedSearchResults = dogBreedSearchResultsState.value
                if (searchQueryState.value.isNotEmpty()) {
                    Text(
                        text = "Search results: ${searchQueryState.value}",
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 8.dp, start = 16.dp)
                    )
                }
                DogBreeds(
                    dogBreeds = dogBreedSearchResults, dogBreedSelected = { breed ->
                        searchScreenViewModel.setSelectedTopDogBreed(breed)
                    }, modifier = Modifier
                )
            }

            SearchView(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                searchText = searchQueryState.value,
                onSearchTextChanged = { query ->
                    searchQueryState.value = query
                    searchScreenViewModel.searchDogBreeds(query)
                },
                onClearClick = {
                    searchQueryState.value = ""
                    searchScreenViewModel.searchDogBreeds("")
                },
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun DogBreeds(
    modifier: Modifier = Modifier,
    dogBreeds: List<DogBreed>,
    dogBreedSelected: (DogBreed) -> Unit = {},
) {
    if (dogBreeds.isNotEmpty()) {

        LazyColumn(
            modifier = modifier.fillMaxWidth(), state = rememberLazyListState()
        ) {

            itemsIndexed(items = dogBreeds) { _, item ->
                DogBreedHighlight(
                    dogBreed = item,
                    onSelected = dogBreedSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    imageHeight = 200.dp
                )
            }
        }
    } else {
        // show no items
    }
}

@ExperimentalCoilApi
@Composable
fun DogBreedHighlight(
    modifier: Modifier = Modifier,
    dogBreed: DogBreed,
    onSelected: (dogBreed: DogBreed) -> Unit,
    imageHeight: Dp = 200.dp,
    showBreedDescription: Boolean = false,
    contentColor: Color? = null
) {
    val logger = Logger.withTag("DogBreedHighlight")
    val contentColorState = remember {
        contentColor ?: getRandomLightColor()
    }

    Column(
        modifier = modifier
            .clickable(onClick = { onSelected(dogBreed) }),
        horizontalAlignment = Alignment.Start
    ) {

        SubcomposeAsyncImage(
            model = dogBreed.imageUrl,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp)
                )
            },
            contentDescription = dogBreed.name,
            modifier = Modifier.height(imageHeight),
            contentScale = if (showBreedDescription) ContentScale.Fit else ContentScale.FillWidth
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(contentColorState)
                .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = dogBreed.name, style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(top = 12.dp)
            )
            if (showBreedDescription) {
                Text(
                    text = dogBreed.bredFor, style = TextStyle(fontSize = 16.sp)
                )
            }
            Text(
                text = dogBreed.breedGroup,
                style = TextStyle(color = contentColorState.darkenColor(), fontSize = 14.sp)
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
                visible = showClearButton, enter = fadeIn(), exit = fadeOut()
            ) {
                IconButton(onClick = {
                    if (searchText.isEmpty()) {
                        focusManager.clearFocus()
                    } else {
                        onClearClick()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close, contentDescription = "Close icon"
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