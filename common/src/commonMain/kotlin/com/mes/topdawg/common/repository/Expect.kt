package com.mes.topdawg.common.repository

import org.koin.core.module.Module

expect fun platformModule(): Module

expect class FileResource(location: String) {
    val json: String?
}

expect object Constants {
    val BreedsLocation: String
}
