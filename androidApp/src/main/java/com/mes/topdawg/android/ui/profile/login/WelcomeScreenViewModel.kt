package com.mes.topdawg.android.ui.profile.login

import androidx.lifecycle.ViewModel
import com.mes.topdawg.common.state.StateMachine
import com.mes.topdawg.common.state.UserProfileEvent
import com.mes.topdawg.common.state.UserProfileState
import com.mes.topdawg.common.state.UserProfileStateMachine

class WelcomeScreenViewModel : ViewModel() {
    val profileState: StateMachine<UserProfileState, UserProfileEvent> = UserProfileStateMachine()
}