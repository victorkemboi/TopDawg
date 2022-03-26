package com.mes.topdawg.common.di

import com.mes.topdawg.common.data.local.TopDawgDatabaseWrapper
import com.mes.topdawg.topdawg.db.TopDawgDatabase
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module =  module {
    single {
        val driver = NativeSqliteDriver(TopDawgDatabase.Schema, "topdawg.db")
        TopDawgDatabaseWrapper(TopDawgDatabase(driver))
    }
    single { Ios.create() }
}