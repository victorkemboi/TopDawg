package com.mes.topdawg.android.ui.dog.breeddetails

import androidx.lifecycle.ViewModel
import com.mes.topdawg.common.data.repository.DogBreedsRepository

class DogBreedDetailsScreenViewModel(
    private val dogBreedsRepository: DogBreedsRepository
): ViewModel() {

    val dogBreed = dogBreedsRepository.fetchAllBreedsAsFlow()

}