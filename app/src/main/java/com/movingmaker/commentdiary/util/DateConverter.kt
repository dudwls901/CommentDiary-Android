package com.movingmaker.commentdiary.util

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

object DateConverter {

    fun getCodaToday() = LocalDateTime.now().minusHours(7).toLocalDate()

    fun ymdFormat(date: LocalDate) = "${String.format("%02d", date.year)}.${String.format("%02d", date.monthValue)}.${String.format("%02d", date.dayOfMonth)}"

    fun ymdToDate(ymd: String): LocalDate{
        val (y,m,d) = ymd.split('.').map{it.toInt()}
        return LocalDate.of(y,m,d)
    }
}

