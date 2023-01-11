package com.movingmaker.domain.model.response

data class Diary(
    val id: Long,
    val userId: Long = -1,
    val title: String,
    val content: String,
    val date: String,
    val deliveryYN: Char,
    val commentList: MutableList<Comment>
)

