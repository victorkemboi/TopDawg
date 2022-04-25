package com.mes.topdawg.common.data.repository

import co.touchlab.kermit.Logger
import com.mes.topdawg.common.data.local.TopDawgDatabaseWrapper
import com.mes.topdawg.common.data.local.entity.UserProfile
import com.mes.topdawg.common.data.local.entity.toUserProfile
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface AuthRepository {
    suspend fun login(userId: Long): UserProfile?
}

class AuthRepositoryImpl : AuthRepository, KoinComponent {
    private val logger = Logger.withTag("BreedsRepository")
    private val topDawgDatabase: TopDawgDatabaseWrapper by inject()
    private val userProfileQueries = topDawgDatabase.instance?.userProfileQueries

    override suspend fun login(userId: Long): UserProfile? {
        logger.i { "Logging in." }
        val userProfile =
            userProfileQueries?.fetchUserProfileById(userId)?.executeAsOneOrNull() ?: return null
        userProfileQueries.login(
            id = null,
            loggedOut = false,
            createdAt = Clock.System.now().toEpochMilliseconds(),
            userProfileId = userProfile.id
        )
        val activeProfileId =
            userProfileQueries.fetchActiveProfile().executeAsOneOrNull()?.userProfileId.toString()
        logger.i {
            """
                userId: $userId  and active profile after login: $activeProfileId
            """.trimIndent()
        }
        return userProfile.toUserProfile()
    }


}