package com.mes.topdawg.common.repository

import co.touchlab.kermit.Logger
import com.mes.topdawg.common.entity.DogBreed
import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import com.mes.topdawg.common.di.TopDawgDatabaseWrapper
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface DogBreedsRepositoryInterface {
    fun fetchAllBreedsAsFlow(): Flow<List<DogBreed>>
    suspend fun fetchBreedByIdAsFlow(id: Long): Flow<DogBreed?>
    suspend fun fetchRandomBreed(): DogBreed
    suspend fun searchDogBreed(query: String): Flow<List<DogBreed>>
    suspend fun dumpBreedsDatabaseInitialData()
}

class DogBreedsRepository : KoinComponent, DogBreedsRepositoryInterface {

    @NativeCoroutineScope
    private val coroutineScope: CoroutineScope = MainScope()
    private val topDawgDatabase: TopDawgDatabaseWrapper by inject()
    private val breedsQueries = topDawgDatabase.instance?.dogbreedsQueries

    val logger = Logger.withTag("BreedsRepository")

    init {
        coroutineScope.launch {
            dumpBreedsDatabaseInitialData()
        }
    }

    override fun fetchAllBreedsAsFlow(): Flow<List<DogBreed>> =
        breedsQueries?.selectAll(
            mapper = dogBreedEntityMapper
        )?.asFlow()?.mapToList() ?: flowOf(emptyList())

    override suspend fun fetchBreedByIdAsFlow(id: Long): Flow<DogBreed?> =
        breedsQueries?.fetchById(id=id)?.asFlow()
            ?.mapToOne()
            ?.map { breed ->
                breed.toDogBreed()
            } ?: flowOf(null)
//            .asFlow()?.map { it. }

    override suspend fun fetchRandomBreed(): DogBreed {
        TODO("Not yet implemented")
    }

    override suspend fun searchDogBreed(query: String): Flow<List<DogBreed>> =
        breedsQueries?.search(
            query = "% $query %",
            mapper = dogBreedEntityMapper
        )?.asFlow()?.mapToList() ?: flowOf(emptyList())

    override suspend fun dumpBreedsDatabaseInitialData() {
        TODO("Not yet implemented")
    }
}
