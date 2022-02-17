package com.movingmaker.commentdiary.view.main.calendardecorator

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.style.ForegroundColorSpan
import com.movingmaker.commentdiary.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

//사이즈, 글자 색 반전 등의 커스텀 필요 없으면 xml에서 처리
class SelectedDateDecorator(context: Activity): DayViewDecorator {
    //drawable 가져오기 패캠에서 찾자
    private val drawable: Drawable = context.resources.getDrawable(R.drawable.date_selector)
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade) {
        //R.drawable.date_selector에서 선택한 날짜만 바뀌게 처리
        view.setSelectionDrawable(drawable)
    }
}