package com.zcs.app.camerax.entity

import java.io.Serializable

class MediaEntity(var type: Int, var path: String) : Serializable {
    companion object {
        const val PIC = 0
        const val VIDEO = 1
    }
}