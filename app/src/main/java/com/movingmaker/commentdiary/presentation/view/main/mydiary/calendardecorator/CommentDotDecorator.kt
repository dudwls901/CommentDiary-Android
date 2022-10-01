package com.movingmaker.commentdiary.presentation.view.main.mydiary.calendardecorator

import android.content.Context
import androidx.core.content.ContextCompat
import com.movingmaker.commentdiary.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class CommentDotDecorator(context: Context, private val dates: List<CalendarDay>) : DayViewDecorator {
    val color = ContextCompat.getColor(context, R.color.dot_green)
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(object: DotSpan(10f,color){})
    }
}