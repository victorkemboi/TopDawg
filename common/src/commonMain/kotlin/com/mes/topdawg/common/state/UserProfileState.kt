@file:JvmName("UserProfileCommonMain")

package com.mes.topdawg.common.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.jvm.JvmName

class UserProfileStateMachine(
    scope: CoroutineScope
) {
    private val _currentState: MutableStateFlow<UserProfileState> =
        MutableStateFlow(UserProfileState.LoggedOut)
    val currentState: StateFlow<UserProfileState> = _currentState.asStateFlow()

    init {
        scope.launch {

        }
    }

    fun transition(event: UserProfileEvent) {
        when (event) {
            UserProfileEvent.InitLogin -> {
                _currentState.update { UserProfileState.LoggingIn }
            }
            UserProfileEvent.OnLogin -> {
                _currentState.update { UserProfileState.LoggedIn }
            }
            UserProfileEvent.InitLogout -> {
                _currentState.update { UserProfileState.LoggingOut }
            }
            UserProfileEvent.OnLogout -> {
                _currentState.update { UserProfileState.LoggedOut }
            }
        }
    }
}

sealed class UserProfileState {
    object LoggedIn : UserProfileState()
    object LoggedOut : UserProfileState()
    object LoggingIn : UserProfileState()
    object LoggingOut : UserProfileState()
}

sealed class UserProfileEvent {
    object InitLogin : UserProfileEvent()
    object OnLogin : UserProfileEvent()
    object InitLogout : UserProfileEvent()
    object OnLogout : UserProfileEvent()
}

sealed class UserProfileSideEffects {}

