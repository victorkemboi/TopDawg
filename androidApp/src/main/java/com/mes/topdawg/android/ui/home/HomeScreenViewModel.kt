package com.mes.topdawg.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mes.topdawg.common.entity.local.DogBreed
import com.mes.topdawg.common.repository.DogBreedsRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val breedsRepository: DogBreedsRepositoryInterface
) : ViewModel() {

    init {
        fetchRandomDogBreed()
    }

    private val randomDogBreedMutableStateFlow: MutableStateFlow<DogBreed?> = MutableStateFlow(null)
    val randomDogBreed = randomDogBreedMutableStateFlow.asStateFlow()

    private fun fetchRandomDogBreed() = viewModelScope.launch(Dispatchers.IO) {
        randomDogBreedMutableStateFlow.value = breedsRepository.fetchRandomBreed()
    }
}