package com.movingmaker.commentdiary.util

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

object DateConverter {

    @JvmStatic
    fun getCodaToday() = LocalDateTime.now().minusHours(7).toLocalDate()

    @JvmStatic
    fun ymdFormat(date: LocalDate) = "${date.year}.${String.format("%02d", date.monthValue)}.${String.format("%02d", date.dayOfMonth)}"

    @JvmStatic
    fun ymFormat(date: LocalDate) = "${date.year}.${String.format("%02d",date.monthValue)}"

    @JvmStatic
    fun ymdToDate(ymd: String): LocalDate{
        val (y,m,d) = ymd.split('.').map{it.toInt()}
        return LocalDate.of(y,m,d)
    }
}

