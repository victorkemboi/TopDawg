package com.mes.topdawg.common.data.local

import com.mes.topdawg.common.data.local.entity.DogBreed
import com.mes.topdawg.database.DogBreedsEntity

val dogBreedEntityMapper: (
    Long, String, String?, String?, String?, String?, String?, String, String?, String?, String?
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
        temperament = temperament ?: "",
    )
}

fun DogBreedsEntity.toDogBreed() =
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