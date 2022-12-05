package com.movingmaker.domain.model.response

data class ReceivedDiary(
    val id: Long?,
    var title: String,
    var content: String,
    var date: String,
    var deliveryYN: Char,
    var tempYN: Char,
    val myComment: MutableList<ReceivedComment>?
)
