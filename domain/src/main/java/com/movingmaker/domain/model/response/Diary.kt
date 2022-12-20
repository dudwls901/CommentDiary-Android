package com.movingmaker.domain.model.response

data class Diary(
    val id: Long,
    val title: String,
    val content: String,
    val date: String,
    val deliveryYN: Char,
    val commentList: MutableList<Comment>
)
