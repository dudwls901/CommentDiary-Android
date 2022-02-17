package com.movingmaker.commentdiary.util

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

object DateConverter {

    //코다의 시간은 7시간 느리게 흘러간다..
    //Wed Feb 16 18:27:06 GMT+09:00 2022
//    val date = Date(System.currentTimeMillis())
//    val dateConvert = SimpleDateFormat("yyyy-MM-dd kk:mm:ss E", Locale("ko", "KR"))
//    val localTime = System.currentTimeMillis()

    fun convertDateToCalendarDay(date : Date) : CalendarDay{
        return CalendarDay.from(date)
    }
    fun convertToDateWithTime(){

    }

    fun convertToCalendarDay(){

    }

    fun convertToDateWithTimeWithDay(){

    }

//    Log.d("aaaaaaa",date.toString())
//    Log.d("aaaaaaaaaaaaa",dateConvert.format(localTime))

}