package com.movingmaker.presentation.util

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDate
import java.time.LocalDateTime

object DateConverter {

    @JvmStatic
    fun getCodaToday(): LocalDate = LocalDateTime.now().minusHours(7).toLocalDate()

    @JvmStatic
    fun getToday() = LocalDateTime.now().toLocalDate()

    @JvmStatic
    fun ymdToDate(ymd: String): LocalDate? {
        return try{
            val (y, m, d) = ymd.split('.').map { it.toInt() }
            LocalDate.of(y, m, d)
        }catch (e: Exception){
            null
        }
    }

    @JvmStatic
    fun ymdToCalendarDay(date: String): CalendarDay?{
        return try {
            val (y, m, d) = date.split('.').map { it.toInt() }
            CalendarDay.from(y,m-1,d)
        }catch (e: Exception){
            null
        }
    }

    @JvmStatic
    fun calenderDayToLocalDate(date: CalendarDay): LocalDate =
        LocalDate.of(date.year, date.month + 1, date.day)


    @JvmStatic
    fun toCalenderDay(date: Any?): CalendarDay {
        return when (date) {
            is String -> {
                val ymd = date.split('.').map { it.toInt() }
                when (ymd.size) {
                    3 -> {
                        CalendarDay.from(ymd[0], ymd[1] - 1, ymd[2])
                    }
                    2 -> {
                        CalendarDay.from(ymd[0], ymd[1] - 1, 1)
                    }
                    else -> CalendarDay.today()
                }
            }
            else -> {
                CalendarDay.today()
            }
        }
    }

    @JvmStatic
    fun ymdFormat(date: Any): String? {
        return when (date) {
            is LocalDate -> {
                "${date.year}.${String.format("%02d", date.monthValue)}.${
                    String.format(
                        "%02d",
                        date.dayOfMonth
                    )
                }"
            }
            is CalendarDay -> {
                "${date.year}.${String.format("%02d", date.month + 1)}.${
                    String.format(
                        "%02d",
                        date.day
                    )
                }"
            }
            else -> {
                null
            }
        }
    }

    //0~11월
    @JvmStatic
    fun ymFormatForCalendarDay(date: Any): String? {
        return when (date) {
            is LocalDate -> {
                "${date.year}.${String.format("%02d", date.monthValue - 1)}"
            }
            is CalendarDay -> {
                "${date.year}.${String.format("%02d", date.month)}"
            }
            else -> {
                null
            }
        }
    }

    //1~12월
    @JvmStatic
    fun ymFormatForLocalDate(date: Any?): String? {
        return when (date) {
            is LocalDate -> {
                "${date.year}.${String.format("%02d", date.monthValue)}"
            }
            is CalendarDay -> {
                "${date.year}.${String.format("%02d", date.month + 1)}"
            }
            is String -> {
                try {
                    date.substring(0, 7)
                } catch (e: Exception) {
                    null
                }
            }
            else -> {
                null
            }
        }
    }

}

