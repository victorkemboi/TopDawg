package com.mes.topdawg.android.di

import com.mes.topdawg.android.ui.home.HomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeScreenViewModel(get()) }
}
