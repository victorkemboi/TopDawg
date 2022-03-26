package com.mes.topdawg.common.data.local.entity

import kotlinx.serialization.Serializable

@Serializable
data class DogBreed(
    val id: Long,
    val bredFor: String,
    val breedGroup: String,
    val height: String,
    val weight: String,
    val imageUrl: String,
    val lifeSpan: String,
    val name: String,
    val origin: String,
    val temperament: String,
)

@Serializable
data class DogBreeds(val result: List<DogBreed>)
