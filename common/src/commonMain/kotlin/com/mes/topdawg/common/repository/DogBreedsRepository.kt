package com.mes.topdawg.common.repository

import co.touchlab.kermit.Logger
import com.mes.topdawg.common.entity.DogBreed
import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import com.mes.topdawg.common.di.TopDawgDatabaseWrapper
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface DogBreedsRepositoryInterface {
    fun fetchAllBreedsAsFlow(): Flow<List<DogBreed>>
    suspend fun fetchBreedByIdAsFlow(id: Long): Flow<DogBreed?>
    suspend fun fetchRandomBreed(): DogBreed
    suspend fun dumpBreedsDatabaseInitialData()
}

class DogBreedsRepository : KoinComponent, DogBreedsRepositoryInterface {

    @NativeCoroutineScope
    private val coroutineScope: CoroutineScope = MainScope()
    private val topDawgDatabase: TopDawgDatabaseWrapper by inject()
    private val breedsQueries = topDawgDatabase.instance?.breedsQueries

    val logger = Logger.withTag("BreedsRepository")

    init {
        coroutineScope.launch {
            dumpBreedsDatabaseInitialData()
        }
    }

    override fun fetchAllBreedsAsFlow(): Flow<List<DogBreed>> =
        breedsQueries?.selectAll(
            mapper = { id, bredFor, breedGroup, height, weight, imageUrl, lifeSpan,
                       name, origin, temperament, searchString ->
                logger.i { "Breed[$id]: $name" }
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
                    searchString = searchString ?: ""
                )
            })?.asFlow()?.mapToList() ?: flowOf(emptyList())

    override suspend fun fetchBreedByIdAsFlow(id: Long): Flow<DogBreed?> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchRandomBreed(): DogBreed {
        TODO("Not yet implemented")
    }

    override suspend fun dumpBreedsDatabaseInitialData() {
        TODO("Not yet implemented")
    }
}
