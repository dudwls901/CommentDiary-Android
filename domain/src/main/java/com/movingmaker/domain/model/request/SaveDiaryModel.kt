package com.movingmaker.domain.model.request

data class SaveDiaryModel(
    val title: String,
    val content: String,
    val date: String,
    val deliveryYN: Char,
    val tempYN: Char,
)