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
import com.movingmaker.commentdiary.model.entity.ReceivedDiary
import com.movingmaker.commentdiary.model.remote.request.*
import com.movingmaker.commentdiary.model.remote.response.*
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

    private var _receivedDiary = MutableLiveData<ReceivedDiary>()
    private var _commentTextCount = MutableLiveData<Int>()

    private var _responseGetReceivedDiary = MutableLiveData<Response<ReceivedDiaryResponse>>()
    private var _responseSaveComment = MutableLiveData<Response<IsSuccessResponse>>()
    private var _responseReportDiary = MutableLiveData<Response<IsSuccessResponse>>()

    val responseGetReceivedDiary: LiveData<Response<ReceivedDiaryResponse>>
        get() = _responseGetReceivedDiary

    val responseSaveComment: LiveData<Response<IsSuccessResponse>>
        get() = _responseSaveComment

    val responseReportDiary: LiveData<Response<IsSuccessResponse>>
        get() = _responseReportDiary

    val receivedDiary: LiveData<ReceivedDiary>
        get() = _receivedDiary

    val commentTextCount: LiveData<Int>
        get() = _commentTextCount

    init {
        _commentTextCount.value = 0
    }

    fun setReceivedDiary(diary: ReceivedDiary){
        _receivedDiary.value =diary
    }

    fun setCommentTextCount(cnt: Int){
        _commentTextCount.value = cnt
    }

    //오늘 날짜로 받은 일기 조회
    suspend fun setResponseGetReceivedDiary() {
        val date = DateConverter.ymdFormat(DateConverter.getCodaToday())
        withContext(viewModelScope.coroutineContext) {
            _responseGetReceivedDiary.value = ReceivedDiaryRepository.INSTANCE.getReceivedDiary(date)
        }
    }

    //코멘트 저장
    suspend fun setResponseSaveComment(content: String){
        val date = DateConverter.ymdFormat(DateConverter.getCodaToday())
        withContext(viewModelScope.coroutineContext){
            _responseSaveComment.value = ReceivedDiaryRepository.INSTANCE.saveComment(
                SaveCommentRequest(
                    id = receivedDiary.value!!.id!!,
                    date = date,
                    content = content
                )
            )
        }
    }

    //일기 신고
    suspend fun setResponseReportDiary(content: String) {
        withContext(viewModelScope.coroutineContext) {
            _responseReportDiary.value = ReceivedDiaryRepository.INSTANCE.reportDiary(
                ReportDiaryRequest(
                    id = receivedDiary.value!!.id!!,
                    content = content
                )
            )
        }
    }

}