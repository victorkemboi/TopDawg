@file:JvmName("UserProfileCommonMain")

package com.mes.topdawg.common.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.jvm.JvmName

class UserProfileStateMachine(initialState: UserProfileState = UserProfileState.LoggedOut) {
    private val _currentState = MutableStateFlow(initialState)
    val currentState: StateFlow<UserProfileState> = _currentState.asStateFlow()

    fun transition(event: UserProfileEvent) {
        when (event) {
            UserProfileEvent.InitLogin -> {
                _currentState.compareAndSet(UserProfileState.LoggedOut, UserProfileState.LoggingIn)
            }
            UserProfileEvent.OnLogin -> {
                _currentState.compareAndSet(UserProfileState.LoggingIn, UserProfileState.LoggingIn)
            }
            UserProfileEvent.InitLogout -> {
                _currentState.compareAndSet(UserProfileState.LoggedOut, UserProfileState.LoggingIn)
            }
            UserProfileEvent.OnLogout -> {
                _currentState.compareAndSet(UserProfileState.LoggedOut, UserProfileState.LoggingIn)
            }
        }
    }
}

sealed class UserProfileState {
    object LoggedIn() : UserProfileState()
    object LoggedOut : UserProfileState()
    object LoggingIn : UserProfileState()
    object LoggingOut : UserProfileState()
}

sealed class UserProfileEvent {
    object InitLogin() : UserProfileEvent()
    object OnLogin : UserProfileEvent()
    object InitLogout : UserProfileEvent()
    object OnLogout : UserProfileEvent()
}

sealed class UserProfileSideEffects {}

