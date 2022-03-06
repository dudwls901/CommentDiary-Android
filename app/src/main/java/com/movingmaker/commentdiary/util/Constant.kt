package com.movingmaker.commentdiary.util

object RetrofitHeaderCondition {
    const val BEARER = "bearer"
    const val ONE_HEADER = "oneHeader"
    const val TWO_HEADER = "twoHeader"
    const val NO_HEADER ="noHeader"
}
object CustomTag{
    val ATAG = "aaaaaaaaa"
}

enum class RESPONSE_STATE{
    OKAY,
    FAIL
}