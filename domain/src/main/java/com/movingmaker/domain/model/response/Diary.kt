package com.movingmaker.domain.model.response

data class Diary(
    val id: Long?,
    var title: String,
    var content: String,
    var date: String,
    var deliveryYN: Char,
    var tempYN: Char? = null,
    val commentList: MutableList<Comment>?
)
