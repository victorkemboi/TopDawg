package com.mes.topdawg.common.data.local.entity

import com.mes.topdawg.database.SqlUserProfile
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: Long, val name: String, val profileImageUrl: String?
)

fun SqlUserProfile.toUserProfile() = UserProfile(
    id = this.id, name = this.name, profileImageUrl = this.profileImageUrl
)


val sqlUserProfileMapper: (
    Long,
    String,
    String?,
) -> UserProfile = { id, name, profileImageUrl ->
    UserProfile(
        id = id, name = name, profileImageUrl = profileImageUrl
    )
}
