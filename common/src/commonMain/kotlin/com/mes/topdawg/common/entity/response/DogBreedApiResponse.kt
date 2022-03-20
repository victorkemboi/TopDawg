package com.mes.topdawg.common.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class DogBreedApiResponses(
    val data: List<DogBreedApiResponse>
)

@Serializable
data class DogBreedApiResponse(
    val bredFor: String,
    val breedGroup: String,
    val height: HeightResponse,
    val id: Long,
    val image: ImageResponse,
    val lifeSpan: String,
    val name: String,
    val origin: String,
    val referenceImageId: String,
    val temperament: String,
    val weight: WeightResponse
)

@Serializable
data class HeightResponse(
    val imperial: String,
    val metric: String
)

@Serializable
data class ImageResponse(
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)


@Serializable
data class WeightResponse(
    val imperial: String,
    val metric: String
)