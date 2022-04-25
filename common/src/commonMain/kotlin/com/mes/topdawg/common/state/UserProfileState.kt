@file:JvmName("UserProfileCommonMain")

package com.mes.topdawg.common.state

import com.mes.topdawg.common.data.local.entity.UserProfile
import com.mes.topdawg.common.data.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.jvm.JvmName

class UserProfileStateMachine(
    scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Main),
    private val authRepository: AuthRepository
) {
    private val _currentState: MutableStateFlow<UserProfileState> =
        MutableStateFlow(UserProfileState.LoggedOut)
    val currentState: StateFlow<UserProfileState> = _currentState.asStateFlow()

    init {
        scope.launch {
            val activeUserProfile = authRepository.fetchLoggedInUser()
            val initialState = if (activeUserProfile != null) {
                UserProfileState.LoggedIn(activeUserProfile)
            } else {
                UserProfileState.LoggedOut
            }
            _currentState.update { initialState }
        }
    }

    suspend fun transition(event: UserProfileEvent) {
        when (event) {
            is UserProfileEvent.InitLogin -> {
                login(event.userProfileId)
            }
            is UserProfileEvent.OnLoginSuccess -> {
                _currentState.update { UserProfileState.LoginSuccess(event.userProfile) }
            }
            is UserProfileEvent.OnLoginFailure -> {
                _currentState.update { UserProfileState.LoginFailed(event.errorMessage) }
            }
            is UserProfileEvent.InitLogout -> {
                _currentState.update { UserProfileState.LoggingOut }
            }
            is UserProfileEvent.OnLogout -> {
                _currentState.update { UserProfileState.LoggedOut }
            }
        }
    }

    private suspend fun login(userProfileId: Long) {
        _currentState.update { UserProfileState.LoggingIn }
        val loggedInUserProfile = authRepository.login(userId = userProfileId)
        val sideEffect = if (loggedInUserProfile != null) {
            UserProfileEvent.OnLoginSuccess(loggedInUserProfile)
        } else {
            UserProfileEvent.OnLoginFailure("The user profile, does not exist!")
        }
        transition(sideEffect)
    }
}

sealed class UserProfileState {
    class LoggedIn(val userProfile: UserProfile) : UserProfileState()
    object LoggedOut : UserProfileState()
    object LoggingIn : UserProfileState()
    object LoggingOut : UserProfileState()
    class LoginSuccess(val userProfile: UserProfile) : UserProfileState()
    class LoginFailed(val errorMessage: String) : UserProfileState()

}

sealed class UserProfileEvent {
    class InitLogin(val userProfileId: Long) : UserProfileEvent()
    class OnLoginSuccess(val userProfile: UserProfile) : UserProfileEvent()
    class OnLoginFailure(val errorMessage: String) : UserProfileEvent()
    object InitLogout : UserProfileEvent()
    object OnLogout : UserProfileEvent()
}

sealed class UserProfileSideEffects {}

