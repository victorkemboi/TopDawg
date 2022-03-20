package com.mes.topdawg.common.entity

import kotlinx.serialization.Serializable

@Serializable
data class Breed(
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
    val searchString: String = "$breedGroup $name $origin",
)
