package com.mes.topdawg.common.data.repository

import co.touchlab.kermit.Logger
import com.mes.topdawg.common.data.local.TopDawgDatabaseWrapper
import com.mes.topdawg.common.data.local.dogBreedEntityMapper
import com.mes.topdawg.common.data.local.entity.DogBreed
import com.mes.topdawg.common.data.local.toDogBreed
import com.mes.topdawg.common.data.remote.model.response.DogBreedApiResponse
import com.mes.topdawg.common.utils.FilePath
import com.mes.topdawg.common.utils.ReadWriteFile
import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface DogBreedsRepositoryInterface {
    fun fetchAllBreedsAsFlow(): Flow<List<DogBreed>>
    suspend fun fetchBreedByIdAsFlow(id: Long): Flow<DogBreed?>
    suspend fun fetchRandomBreed(): DogBreed?
    fun searchDogBreed(query: String): Flow<List<DogBreed>>
    suspend fun dumpDogBreedsDatabaseInitialData()
    suspend fun dumpDogBreedsToDatabase()
}

@ExperimentalSerializationApi
class DogBreedsRepository : KoinComponent, DogBreedsRepositoryInterface {

    @NativeCoroutineScope
    private val coroutineScope: CoroutineScope = MainScope()
    private val topDawgDatabase: TopDawgDatabaseWrapper by inject()
    private val dogBreedsQueries = topDawgDatabase.instance?.dogBreedsQueries

    private val logger = Logger.withTag("BreedsRepository")

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
            logger.i { "Breed: $breed" }
            breed.toDogBreed()
        } ?: flowOf(null)

    override suspend fun fetchRandomBreed(): DogBreed? {
        val randomId =
            (0..(dogBreedsQueries?.countAll()?.asFlow()?.mapToOne()?.first() ?: 0)).random()
        logger.i { "Random id: $randomId" }
        if (randomId < 1) return null
        return dogBreedsQueries?.fetchById(randomId)?.executeAsOneOrNull()?.toDogBreed()
    }

    override fun searchDogBreed(query: String): Flow<List<DogBreed>> =
        if (query.isEmpty()) {
            fetchAllBreedsAsFlow()
        } else {
            dogBreedsQueries?.search(
                query = "%$query%", mapper = dogBreedEntityMapper
            )?.asFlow()?.mapToList() ?: flowOf(emptyList())
        }

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
        val breedsJson = ReadWriteFile().read(resId = FilePath.Breeds.toIntOrNull())
        logger.i { "Breeds read from file." }
        if (breedsJson == null) return
        logger.i { "Breeds from file string is not null" }
        val breeds =
            json.decodeFromString<List<DogBreedApiResponse>>(breedsJson.trimIndent())
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
