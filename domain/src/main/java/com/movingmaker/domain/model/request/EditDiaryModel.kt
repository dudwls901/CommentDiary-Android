package com.movingmaker.domain.model.request

data class EditDiaryModel(
    val title: String,
    val content: String,
    val tempYN: Char = 'N',
)