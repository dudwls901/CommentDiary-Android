package com.movingmaker.domain.util


fun String.ymFormatForString(): String {
    return try {
        val (y, m, _) = this.split(".")
        if (y == "") "unknown error" else "$y.$m"
    }catch (e: Exception){
        e.message?:""
    }
}