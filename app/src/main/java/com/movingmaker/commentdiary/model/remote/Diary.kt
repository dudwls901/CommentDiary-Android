package com.movingmaker.commentdiary.model.remote

data class Diary(
    val content: String,
    val deliveryYN: Char,
    val createdAt: String
)
