package com.zcs.app.camerax

import java.io.File

fun File.isAddCamera(): Boolean {
    return this.absolutePath == "/ADD" || this.path == "ADD"
}

// 为File扩展一个方法，用于判断当前文件是否是图片
fun File.isImage(): Boolean {
    if (!this.isFile || this.length() == 0L)
        return false

    val path = this.absolutePath
    var type = ""
    val index: Int = path.lastIndexOf(".")
    if (index != -1) {
        type = path.substring(index + 1).toLowerCase()
    }
    return type in listOf(
        "png", "jpg", "jpeg", "bmp", "webp"
    )
}

// 为File扩展一个方法，用于判断当前文件是否是视频
fun File.isVideo(): Boolean {
    if (!this.isFile || this.length() == 0L)
        return false

    val path = this.absolutePath
    var type = ""
    val index: Int = path.lastIndexOf(".")
    if (index != -1) {
        type = path.substring(index + 1).toLowerCase()
    }
    return type in listOf(
        "3gp", "mp4"
    )
}