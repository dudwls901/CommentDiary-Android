package com.movingmaker.commentdiary.viewmodel.mydiary

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.model.entity.Diary
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.model.repository.MyDiaryRepository
import com.movingmaker.commentdiary.model.repository.MyPageRepository
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.*

class MyDiaryViewModel : ViewModel() {
    private var _aloneDiary = MutableLiveData<List<CalendarDay>>()
    private var _commentDiary = MutableLiveData<List<CalendarDay>>()
    private var _monthDiaries = MutableLiveData<List<Diary>>()

    //api response
    private var _responseGetMonthDiary = MutableLiveData<Response<DiaryListResponse>>()

    val aloneDiary: LiveData<List<CalendarDay>>
        get() = _aloneDiary

    val commentDiary: LiveData<List<CalendarDay>>
        get() = _commentDiary

    val monthDiaries: LiveData<List<Diary>>
        get() = _monthDiaries

    val responseGetMonthDiary: LiveData<Response<DiaryListResponse>>
        get() = _responseGetMonthDiary


    init {
        _aloneDiary.value = emptyList()
        _commentDiary.value = emptyList()
        _monthDiaries.value = emptyList()
    }

    private fun setAloneDiary(list: List<CalendarDay>) {
        Log.d("mydiaryviewmodel", "setMonthDiaries: ${list.size}")
        _aloneDiary.value = list
    }

    private fun setCommentDiary(list: List<CalendarDay>) {
        Log.d("mydiaryviewmodel", "setMonthDiaries: ${list.size}")
        _commentDiary.value = list
    }

    fun setMonthDiaries(list: List<Diary>) {
        _monthDiaries.value = list

        val aloneDiary = ArrayList<CalendarDay>()
        val commentDiary = ArrayList<CalendarDay>()

        for (selectedDiary in list) {
            val ymd = selectedDiary.date
            val (year, month, day) = ymd.split('.').map { it.toInt() }
            if (selectedDiary.deliveryYN == "Y") {
                commentDiary.add(CalendarDay.from(year, month-1, day))
            } else {
                aloneDiary.add(CalendarDay.from(year, month-1, day))
            }

        }

        //그냥 실행하면 background thread로 작업돼서 컴파일 에러
        //livedata의 setValue는 백그라운드 스레드로 작업 불가
        setCommentDiary(commentDiary.toList())
        setAloneDiary(aloneDiary.toList())

    }

    suspend fun setResponseGetMonthDiary(date: String) {
        withContext(viewModelScope.coroutineContext) {
            _responseGetMonthDiary.value = MyDiaryRepository.INSTANCE.getMonthDiary(date)
        }
    }
}