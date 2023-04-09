package com.movingmaker.presentation.view.main.mydiary.calendardecorator

import android.graphics.Typeface
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.movingmaker.presentation.util.getCodaToday
import com.movingmaker.presentation.util.toCalenderDay
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class TodayDotDecorator :
    DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day == toCalenderDay(getCodaToday())
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(object : StyleSpan(Typeface.BOLD) {})
        view.addSpan(object : RelativeSizeSpan(1.1f) {})
    }
}