package com.mes.topdawg.common.data.local.entity

import com.mes.topdawg.database.SqlDogBreeds
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
    val temperament: String
)

val sqlDogBreedMapper: (
    Long,
    String,
    String?,
    String?,
    String?,
    String?,
    String?,
    String,
    String?,
    String?,
    String?
) -> DogBreed = { id, bredFor, breedGroup, height, weight, imageUrl, lifeSpan,
                  name, origin, temperament, _ ->
    DogBreed(
        id = id,
        bredFor = bredFor,
        breedGroup = breedGroup ?: "",
        height = height ?: "",
        weight = weight ?: "",
        imageUrl = imageUrl ?: "",
        lifeSpan = lifeSpan ?: "",
        name = name,
        origin = origin ?: "",
        temperament = temperament ?: ""
    )
}

fun SqlDogBreeds.toDogBreed() =
    DogBreed(
        id = this.id,
        bredFor = this.bredFor,
        breedGroup = this.breedGroup ?: "",
        height = this.height ?: "",
        weight = this.weight ?: "",
        imageUrl = this.imageUrl ?: "",
        lifeSpan = this.lifeSpan ?: "",
        name = this.name,
        origin = this.origin ?: "",
        temperament = this.temperament ?: ""
    )
