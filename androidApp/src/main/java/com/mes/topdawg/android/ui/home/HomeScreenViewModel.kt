package com.mes.topdawg.android.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mes.topdawg.common.entity.local.DogBreed
import com.mes.topdawg.common.repository.DogBreedsRepositoryInterface
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val breedsRepository: DogBreedsRepositoryInterface
) : ViewModel() {

    init {
        searchDogBreeds("")
    }

    val searchQueryState = mutableStateOf("")

    private val topDogBreedMutableStateFlow: MutableStateFlow<DogBreed?> = MutableStateFlow(null)
    val topDogBreed = topDogBreedMutableStateFlow.asStateFlow()

    var dogBreedSearchResults = MutableStateFlow<List<DogBreed>>(emptyList())

    fun fetchRandomDogBreed() = viewModelScope.launch {
        topDogBreedMutableStateFlow.value = breedsRepository.fetchRandomBreed()
    }

    fun searchDogBreeds(query: String) =
        viewModelScope.launch {
            breedsRepository.searchDogBreed(query = query).collectLatest {
                dogBreedSearchResults.value = it
            }
        }

    fun setSelectedTopDogBreed(dogBreed: DogBreed) {
        topDogBreedMutableStateFlow.value = dogBreed
    }


}