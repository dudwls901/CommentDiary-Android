package com.movingmaker.commentdiary.model.remote

import com.prolificinteractive.materialcalendarview.CalendarDay

data class Diary(
    val content: String,
    val deliveryYN: Char,
    val createdAt: String
)
