package com.mes.topdawg.common.utils

import android.content.Context
import okio.FileSystem
import okio.Path.Companion.toPath
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AndroidUtils

actual class ReadWriteFile : KoinComponent {
    private val context: Context by inject()

    actual fun read(
        path: String?,
        filename: String?,
        resId: Int?
    ): String? {
        return if (resId != null) {
            context.resources.openRawResource(resId).bufferedReader().use { it.readText() }
        } else {
            FileSystem.SYSTEM.read("$path/$filename".toPath()) {
                readUtf8()
            }
        }
    }
}
