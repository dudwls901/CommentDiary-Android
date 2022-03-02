package com.movingmaker.commentdiary.viewmodel.mydiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.model.repository.MyDiaryRepository
import com.movingmaker.commentdiary.model.repository.MyPageRepository
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.withContext
import retrofit2.Response

class CalendarViewModel : ViewModel() {
    private var _aloneDiary = MutableLiveData<List<CalendarDay>>()
    private var _commentDiary = MutableLiveData<List<CalendarDay>>()

    //api response
    private var _responseGetMonthDiary = MutableLiveData<Response<DiaryListResponse>>()

    val aloneDiary : LiveData<List<CalendarDay>>
        get() = _aloneDiary

    val commentDiary : LiveData<List<CalendarDay>>
        get() = _commentDiary

    val responseGetMonthDiary: LiveData<Response<DiaryListResponse>>
        get() = _responseGetMonthDiary

    fun setAloneDiary(list : List<CalendarDay>){
        _aloneDiary.value = list
    }
    fun setCommentDiary(list : List<CalendarDay>){
        _commentDiary.value = list
    }

    suspend fun setResponseGetMonthDiary(date: String) {
        withContext(viewModelScope.coroutineContext) {
            _responseGetMonthDiary.value = MyDiaryRepository.INSTANCE.getMonthDiary(date)
        }
    }
}