package com.movingmaker.presentation.util

inline fun <reified T> combineList(
    list1: List<T>,
    list2: List<T>
): List<T> {
    return MutableList(list1.size + list2.size) {
        if (it < list1.size) list1[it]
        else list2[it - list1.size]
    }
}

fun Char.getDiaryType(): DIARY_TYPE{
    return when(this){
        'Y' -> DIARY_TYPE.COMMENT_DIARY
        else -> DIARY_TYPE.ALONE_DIARY
    }
}