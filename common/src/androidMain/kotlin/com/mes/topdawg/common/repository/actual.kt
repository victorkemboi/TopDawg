package com.mes.topdawg.common.repository

import android.content.Context
import com.mes.topdawg.R
import com.mes.topdawg.common.di.TopDawgDatabaseWrapper
import com.mes.topdawg.topdawg.db.TopDawgDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ktor.client.engine.android.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module
import java.io.StringWriter
import java.io.Writer


actual fun platformModule() = module {
    single {
        val driver =
            AndroidSqliteDriver(TopDawgDatabase.Schema, get(), "topdawg.db")

        TopDawgDatabaseWrapper(TopDawgDatabase(driver))
    }
    single { Android.create() }
}

actual class FileResource actual constructor(location: String) : KoinComponent {
    private val context: Context by inject()
    actual val json: String? =
        context.resources.openRawResource(location.toInt()).bufferedReader().use { it.readText() }

}

actual object Constants {
    actual val BreedsLocation: String = R.raw.breeds.toString()
}