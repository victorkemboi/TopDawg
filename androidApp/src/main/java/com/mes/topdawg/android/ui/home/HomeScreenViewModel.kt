package com.mes.topdawg.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mes.topdawg.common.repository.DogBreedsRepositoryInterface
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HomeScreenViewModel(
    private val breedsRepository: DogBreedsRepositoryInterface
) : ViewModel() {

    val randomBreed = breedsRepository.fetchAllBreedsAsFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

}