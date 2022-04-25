package com.mes.topdawg.android.ui.profile.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mes.topdawg.common.state.StateMachine
import com.mes.topdawg.common.state.UserProfileEvent
import com.mes.topdawg.common.state.UserProfileState
import com.mes.topdawg.common.state.UserProfileStateMachine
import kotlinx.coroutines.launch

class WelcomeScreenViewModel : ViewModel() {
    private val profileStateMachine: StateMachine<UserProfileState, UserProfileEvent> =
        UserProfileStateMachine()

    val uiState = profileStateMachine.currentState

    fun transition(event: UserProfileEvent) {
        viewModelScope.launch {
            profileStateMachine.transition(event)
        }
    }
}