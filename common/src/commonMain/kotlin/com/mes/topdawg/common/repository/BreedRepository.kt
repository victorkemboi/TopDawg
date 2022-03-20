package com.mes.topdawg.common.repository

import co.touchlab.kermit.Logger
import com.mes.topdawg.common.entity.Breed
import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import com.mes.topdawg.common.di.TopDawgDatabaseWrapper
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface BreedRepositoryInterface {
    fun fetchAllBreedsAsFlow(): Flow<List<Breed>>
    suspend fun dumpBreedsDatabaseInitialData()
}

class BreedRepository : KoinComponent, BreedRepositoryInterface {

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

    companion object {
        private const val POLL_INTERVAL = 10000L
    }

    override fun fetchAllBreedsAsFlow(): Flow<List<Breed>> =
        breedsQueries?.selectAll(
            mapper = { id, bredFor, breedGroup, height, weight, imageUrl, lifeSpan,
                       name, origin, temperament, searchString ->
                Breed(
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

    override suspend fun dumpBreedsDatabaseInitialData() {
        TODO("Not yet implemented")
    }
}
