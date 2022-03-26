package com.mes.topdawg.common.utils

import okio.FileSystem
import okio.Path.Companion.toPath

actual class ReadWriteFile {
    actual fun read(
        path: String?,
        filename: String?,
        resId: Int?,
    ): String? =
        FileSystem.SYSTEM.read("$path/$filename".toPath()) {
            readUtf8()
        }
}