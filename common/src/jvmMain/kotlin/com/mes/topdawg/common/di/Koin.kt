package com.mes.topdawg.common.di

import com.mes.topdawg.common.data.local.TopDawgDatabaseWrapper
import com.mes.topdawg.topdawg.db.TopDawgDatabase
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.ktor.client.engine.java.*
import org.koin.dsl.module

actual fun platformModule() = module {
    single {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            .also { TopDawgDatabase.Schema.create(it) }
        TopDawgDatabaseWrapper(TopDawgDatabase(driver))
    }
    single { Java.create() }
}
