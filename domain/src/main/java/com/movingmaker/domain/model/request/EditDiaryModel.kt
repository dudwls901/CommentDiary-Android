package com.movingmaker.domain.model.request

data class EditDiaryModel(
    val title: String,
    val content: String,
    val deliveryYN: Char,
    val tempYN: Char = 'N',
)