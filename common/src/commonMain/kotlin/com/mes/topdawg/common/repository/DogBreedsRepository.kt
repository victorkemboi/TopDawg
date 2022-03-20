package com.mes.topdawg.common.repository

import co.touchlab.kermit.Logger
import com.mes.topdawg.common.di.TopDawgDatabaseWrapper
import com.mes.topdawg.common.entity.local.DogBreed
import com.mes.topdawg.common.entity.local.DogBreeds
import com.mes.topdawg.common.entity.dogBreedEntityMapper
import com.mes.topdawg.common.entity.response.DogBreedApiResponse
import com.mes.topdawg.common.entity.response.DogBreedApiResponses
import com.mes.topdawg.common.entity.toDogBreed
import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface DogBreedsRepositoryInterface {
    fun fetchAllBreedsAsFlow(): Flow<List<DogBreed>>
    suspend fun fetchBreedByIdAsFlow(id: Long): Flow<DogBreed?>
    suspend fun fetchRandomBreed(): DogBreed?
    suspend fun searchDogBreed(query: String): Flow<List<DogBreed>>
    suspend fun dumpDogBreedsDatabaseInitialData()
    suspend fun dumpDogBreedsToDatabase()
}

@ExperimentalSerializationApi
class DogBreedsRepository : KoinComponent, DogBreedsRepositoryInterface {

    @NativeCoroutineScope
    private val coroutineScope: CoroutineScope = MainScope()
    private val topDawgDatabase: TopDawgDatabaseWrapper by inject()
    private val dogBreedsQueries = topDawgDatabase.instance?.dogBreedsQueries

    val logger = Logger.withTag("BreedsRepository")

    init {
        coroutineScope.launch {
            dumpDogBreedsDatabaseInitialData()
        }
    }

    override fun fetchAllBreedsAsFlow(): Flow<List<DogBreed>> = dogBreedsQueries?.selectAll(
        mapper = dogBreedEntityMapper
    )?.asFlow()?.mapToList() ?: flowOf(emptyList())

    override suspend fun fetchBreedByIdAsFlow(id: Long): Flow<DogBreed?> =
        dogBreedsQueries?.fetchById(id = id)?.asFlow()?.mapToOne()?.map { breed ->
            breed.toDogBreed()
        } ?: flowOf(null)

    override suspend fun fetchRandomBreed(): DogBreed? {
        val randomId =
            (0..(dogBreedsQueries?.countAll()?.asFlow()?.mapToOne()?.first() ?: 0)).random()
        logger.i { "Random id: $randomId" }
        if (randomId < 1) return null
        return dogBreedsQueries?.fetchById(id = randomId)?.asFlow()?.mapToOne()?.map { breed ->
            breed.toDogBreed()
        }?.firstOrNull()
    }


    override suspend fun searchDogBreed(query: String): Flow<List<DogBreed>> =
        dogBreedsQueries?.search(
            query = "% $query %", mapper = dogBreedEntityMapper
        )?.asFlow()?.mapToList() ?: flowOf(emptyList())

    override suspend fun dumpDogBreedsDatabaseInitialData() {
        dogBreedsQueries?.deleteAll()
        dumpDogBreedsToDatabase()
    }

    private val json: Json by lazy {
        Json {
            explicitNulls = true
            ignoreUnknownKeys = true
        }
    }

    override suspend fun dumpDogBreedsToDatabase() {
        val breedsJson = FileResource(Constants.BreedsLocation)
        logger.i { "Breeds read from file." }
        if (breedsJson.json != null) {
            logger.i { "Breeds from file is not null" }
            val breeds =
                json.decodeFromString<List<DogBreedApiResponse>>(breedsJson.json.trimIndent())
            breeds.forEach {
                dogBreedsQueries?.insertItem(
                    id = it.id,
                    bredFor = it.bred_for ?: "The information is currently not available.",
                    breedGroup = it.breed_group,
                    height = it.height.metric,
                    weight = it.weight.metric,
                    imageUrl = it.image.url,
                    lifeSpan = it.life_span,
                    name = it.name,
                    origin = it.origin,
                    temperament = it.temperament,
                    searchString = "${it.breed_group} ${it.name} ${it.origin}"
                )
            }
        }
    }

}
