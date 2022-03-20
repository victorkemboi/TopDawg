package com.mes.topdawg.common.repository

import co.touchlab.kermit.Logger
import com.mes.topdawg.common.di.TopDawgDatabaseWrapper
import com.mes.topdawg.common.entity.local.DogBreed
import com.mes.topdawg.common.entity.local.DogBreeds
import com.mes.topdawg.common.entity.dogBreedEntityMapper
import com.mes.topdawg.common.entity.response.DogBreedApiResponses
import com.mes.topdawg.common.entity.toDogBreed
import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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

    override suspend fun fetchRandomBreed(): DogBreed? = dogBreedsQueries?.fetchById(
        id = (1..dogBreedsQueries.countAll().asFlow().mapToOne().first()).random()
    )?.asFlow()?.mapToOne()?.map { breed ->
        breed.toDogBreed()
    }?.firstOrNull()

    override suspend fun searchDogBreed(query: String): Flow<List<DogBreed>> =
        dogBreedsQueries?.search(
            query = "% $query %", mapper = dogBreedEntityMapper
        )?.asFlow()?.mapToList() ?: flowOf(emptyList())

    override suspend fun dumpDogBreedsDatabaseInitialData() {
        dogBreedsQueries?.deleteAll()
    }

    override suspend fun dumpDogBreedsToDatabase() {
        dogBreedsQueries?.deleteAll()
        val breedsJson = FileResource(Constants.BreedsLocation)
        logger.i { "Breeds read from file." }
        if (breedsJson.json != null) {
            logger.i { "Breeds from file is not null" }
            val breeds =
                Json.decodeFromString(DogBreedApiResponses.serializer(), breedsJson.json).data
            breeds.forEach {
                logger.i { "Saving: ${it.name}" }
                dogBreedsQueries?.insertItem(
                    id = it.id,
                    bredFor = it.bredFor,
                    breedGroup = it.breedGroup,
                    height = it.height.metric,
                    weight = it.weight.metric,
                    imageUrl = it.image.url,
                    lifeSpan = it.lifeSpan,
                    name = it.name,
                    origin = it.origin,
                    temperament = it.temperament,
                    searchString = "${it.breedGroup} ${it.name} ${it.origin}"
                )
            }
        }
    }

}
