package com.mes.topdawg.common.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class DogBreedApiResponses(
    val data: List<DogBreedApiResponse>
)

@Serializable
data class DogBreedApiResponse(
    val height: HeightResponse,
    val id: Long,
    val image: ImageResponse,
    val life_span: String,
    val name: String,
    val reference_image_id: String,
    val temperament: String? = null,
    val weight: WeightResponse,
    val bred_for: String? = null,
    val origin: String? = null,
    val breed_group: String? = null,
    val country_code: String? = null
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
