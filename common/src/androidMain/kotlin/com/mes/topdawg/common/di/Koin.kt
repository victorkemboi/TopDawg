@file:JvmName("KoinCommonAndroid")
package com.mes.topdawg.common.di

import com.mes.topdawg.common.data.local.TopDawgDatabaseWrapper
import com.mes.topdawg.topdawg.db.TopDawgDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ktor.client.engine.android.*
import org.koin.dsl.module

actual fun platformModule() = module {
    single {
        val driver =
            AndroidSqliteDriver(TopDawgDatabase.Schema, get(), "topdawg.db")

        TopDawgDatabaseWrapper(TopDawgDatabase(driver))
    }
    single { Android.create() }
}