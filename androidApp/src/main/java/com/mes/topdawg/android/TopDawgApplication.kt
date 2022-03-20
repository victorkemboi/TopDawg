package com.mes.topdawg.android

import android.app.Application
import co.touchlab.kermit.Logger
import com.mes.topdawg.android.di.appModule
import com.mes.topdawg.common.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import java.io.File

class TopDawgApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            // https://github.com/InsertKoinIO/koin/issues/1188
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.INFO)
            androidContext(this@TopDawgApplication)
            modules(appModule)
        }

        Logger.i { "TopDawg app started." }
    }

}