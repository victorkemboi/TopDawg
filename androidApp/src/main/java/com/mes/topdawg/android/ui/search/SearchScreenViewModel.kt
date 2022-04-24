package com.mes.topdawg.android.ui.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mes.topdawg.common.data.local.entity.DogBreed
import com.mes.topdawg.common.data.repository.DogBreedsRepositoryInterface
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchScreenViewModel(
    private val breedsRepository: DogBreedsRepositoryInterface
) : ViewModel() {

    init {
        searchDogBreeds("")
    }

    val searchQueryState = mutableStateOf("")

    var dogBreedSearchResults = MutableStateFlow<List<DogBreed>>(emptyList())

    fun searchDogBreeds(query: String) =
        viewModelScope.launch {
            breedsRepository.searchDogBreed(query = query).collectLatest {
                dogBreedSearchResults.value = it
            }
        }
}
