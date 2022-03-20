package com.mes.topdawg

actual class Platform actual constructor() {
    actual val platform: String
        get() = "On Jvm platform"
}