package com.movingmaker.commentdiary.viewmodel.mydiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay

class CalendarViewModel : ViewModel() {
    private var _aloneDiary = MutableLiveData<List<CalendarDay>>()
    private var _commentDiary = MutableLiveData<List<CalendarDay>>()

    val aloneDiary : LiveData<List<CalendarDay>>
        get() = _aloneDiary

    val commentDiary : LiveData<List<CalendarDay>>
        get() = _commentDiary

    fun setAloneDiary(list : List<CalendarDay>){
        _aloneDiary.value = list
    }
    fun setCommentDiary(list : List<CalendarDay>){
        _commentDiary.value = list
    }
}