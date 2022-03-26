package com.mes.topdawg.common.utils

expect class ReadWriteFile() {
    fun read(path: String? = null, filename: String? = null, resId: Int? = null): String?
}