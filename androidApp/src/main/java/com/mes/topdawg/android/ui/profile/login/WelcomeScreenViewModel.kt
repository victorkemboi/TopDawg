package com.mes.topdawg.android.ui.profile.login

import androidx.lifecycle.ViewModel
import com.mes.topdawg.common.state.UserProfileEvent
import com.mes.topdawg.common.state.UserProfileState
import com.mes.topdawg.common.state.UserProfileStateMachine
import com.mes.topdawg.common.state.UserProfileStateMachineImpl

class WelcomeScreenViewModel : ViewModel() {
    private val profileStateMachine: UserProfileStateMachine<UserProfileState, UserProfileEvent> =
        UserProfileStateMachineImpl()

    val profileState = profileStateMachine.currentState
}