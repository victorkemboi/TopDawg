package com.mes.topdawg.common.di

import com.mes.topdawg.common.data.local.TopDawgDatabaseWrapper
import io.ktor.client.engine.js.*
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single {
        TopDawgDatabaseWrapper(null)
    }
    single { Js.create() }
}