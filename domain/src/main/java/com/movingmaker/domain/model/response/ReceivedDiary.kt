package com.movingmaker.domain.model.response

data class ReceivedDiary(
    val id: Long,
    val title: String,
    val content: String,
    val date: String,
    val deliveryYN: Char,
    val myComment: MutableList<ReceivedComment>
)
