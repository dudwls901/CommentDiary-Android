package com.movingmaker.presentation.view.main.mydiary.calendardecorator

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.ContextCompat
import com.movingmaker.presentation.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

//사이즈, 글자 색 반전 등의 커스텀 필요 없으면 xml에서 처리
class SelectedDateDecorator(context: Context) : DayViewDecorator {
    @SuppressLint("UseCompatLoadingForDrawables")
    private val drawable = ContextCompat.getDrawable(context, R.drawable.selector_calendar_date)
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade) {
        //R.drawable.date_selector에서 선택한 날짜만 바뀌게 처리
        if (drawable != null) {
            view.setSelectionDrawable(drawable)
        }
    }
}