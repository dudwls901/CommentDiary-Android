package com.movingmaker.commentdiary.viewmodel.receiveddiary

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.model.entity.Comment
import com.movingmaker.commentdiary.model.entity.Diary
import com.movingmaker.commentdiary.model.entity.DiaryId
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.model.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.model.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.model.remote.response.DiaryResponse
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.SaveDiaryResponse
import com.movingmaker.commentdiary.model.repository.GatherDiaryRepository
import com.movingmaker.commentdiary.model.repository.MyDiaryRepository
import com.movingmaker.commentdiary.model.repository.MyPageRepository
import com.movingmaker.commentdiary.model.repository.ReceivedDiaryRepository
import com.movingmaker.commentdiary.util.DateConverter
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.*

class ReceivedDiaryViewModel : ViewModel() {
    private var _responseGetReceivedDiary = MutableLiveData<Response<DiaryResponse>>()
    private var _receivedDiary = MutableLiveData<Diary>()
    private var _commentTextCount = MutableLiveData<Int>()

    val responseGetReceivedDiary: LiveData<Response<DiaryResponse>>
        get() = _responseGetReceivedDiary

    val receivedDiary: LiveData<Diary>
        get() = _receivedDiary

    val commentTextCount: LiveData<Int>
        get() = _commentTextCount

    init {
        _commentTextCount.value = 0
    }

    fun setReceivedDiary(diary: Diary){
        _receivedDiary.value =diary
    }

    fun setCommentTextCount(cnt: Int){
        _commentTextCount.value = cnt
    }

    //오늘 날짜로 받은 일기 조회
    suspend fun setResponseGetReceivedDiary() {
        val date = DateConverter.ymdFormat(DateConverter.getCodaToday())
        Log.d("receivedDiary", "setResponseGetMonthDiary $date ")
        withContext(viewModelScope.coroutineContext) {
            _responseGetReceivedDiary.value = ReceivedDiaryRepository.INSTANCE.getReceivedDiary(date)
        }
    }
}