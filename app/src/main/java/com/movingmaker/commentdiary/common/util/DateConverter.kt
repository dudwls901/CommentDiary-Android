package com.movingmaker.commentdiary.common.util

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDate
import java.time.LocalDateTime

const val TAG = "DateConverter"
object DateConverter {

    @JvmStatic
    fun getCodaToday(): LocalDate = LocalDateTime.now().minusHours(7).toLocalDate()

    @JvmStatic
    fun getToday() = LocalDateTime.now().toLocalDate()

    @JvmStatic
    fun toCalenderDay(date: Any?): CalendarDay {
        return when(date){
            is String ->{
                val ymd = date.split('.').map{it.toInt()}
                when(ymd.size){
                    3 ->{
                        CalendarDay.from(ymd[0],ymd[1]-1,ymd[2])
                    }
                    2 ->{
                        CalendarDay.from(ymd[0],ymd[1]-1, 1)
                    }
                    else -> CalendarDay.today()
                }
            }
            else ->{
                CalendarDay.today()
            }
        }
    }

    @JvmStatic
    fun ymdFormat(date: Any) : String?{
        return when(date){
            is LocalDate ->{
                "${date.year}.${String.format("%02d", date.monthValue)}.${String.format("%02d", date.dayOfMonth)}"
            }
            is CalendarDay ->{
                "${date.year}.${String.format("%02d", date.month+1)}.${String.format("%02d", date.day)}"
            }
            else ->{
                null
            }
        }
    }

    @JvmStatic
    fun ymFormat(date: Any) : String?{
        return when(date){
            is LocalDate -> {
                "${date.year}.${String.format("%02d",date.monthValue)}"
            }
            is CalendarDay -> {
                "${date.year}.${String.format("%02d", date.month+1)}"
            }
            else -> {
                null
            }
        }
    }

    @JvmStatic
    fun ymdToDate(ymd: String): LocalDate{
        val (y,m,d) = ymd.split('.').map{it.toInt()}
        return LocalDate.of(y,m,d)
    }
}

