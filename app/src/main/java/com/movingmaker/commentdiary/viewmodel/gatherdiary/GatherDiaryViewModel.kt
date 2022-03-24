package com.movingmaker.commentdiary.viewmodel.gatherdiary

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
import com.movingmaker.commentdiary.model.remote.request.ReportCommentRequest
import com.movingmaker.commentdiary.model.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.SaveDiaryResponse
import com.movingmaker.commentdiary.model.repository.GatherDiaryRepository
import com.movingmaker.commentdiary.model.repository.MyDiaryRepository
import com.movingmaker.commentdiary.model.repository.MyPageRepository
import com.movingmaker.commentdiary.util.DateConverter
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.*

class GatherDiaryViewModel : ViewModel() {
    private var _diaryList = MutableLiveData<List<Diary>>()
    private var _selectedMonth = MutableLiveData<String>()
    private var _responseGetMonthDiary = MutableLiveData<Response<DiaryListResponse>>()
    private var _responseGetAllDiary = MutableLiveData<Response<DiaryListResponse>>()
    private var _responseLikeComment = MutableLiveData<Response<IsSuccessResponse>>()
    private var _responseReportComment = MutableLiveData<Response<IsSuccessResponse>>()


    val diaryList: LiveData<List<Diary>>
        get() = _diaryList

    val selectedMonth: LiveData<String>
        get() = _selectedMonth

    val responseGetMonthDiary: LiveData<Response<DiaryListResponse>>
        get() = _responseGetMonthDiary

    val responseGetAllDiary: LiveData<Response<DiaryListResponse>>
        get() = _responseGetAllDiary

    val responseLikeComment: LiveData<Response<IsSuccessResponse>>
        get() = _responseLikeComment

    val responseReportComment: LiveData<Response<IsSuccessResponse>>
        get() = _responseReportComment

    init {
        _diaryList.value = emptyList()
        _selectedMonth.value = DateConverter.ymFormat(DateConverter.getCodaToday())
    }

    fun setDiaryList(list: List<Diary>) {
        _diaryList.value = list
    }

    fun setSelectedMonth(date: String){
        _selectedMonth.value = date
    }

    suspend fun setResponseGetMonthDiary(date: String) {
        withContext(viewModelScope.coroutineContext) {
            _responseGetMonthDiary.value = GatherDiaryRepository.INSTANCE.getMonthDiary(date)
        }
    }

    suspend fun setResponseGetAllDiary() {
        withContext(viewModelScope.coroutineContext) {
            _responseGetAllDiary.value = GatherDiaryRepository.INSTANCE.getAllDiary()
        }
    }

    suspend fun setResponseReportComment(reportCommentRequest: ReportCommentRequest) {
        withContext(viewModelScope.coroutineContext) {
            _responseReportComment.value = GatherDiaryRepository.INSTANCE.reportComment( reportCommentRequest )
        }
    }

    suspend fun setResponseLikeComment(commentId: Long) {
        withContext(viewModelScope.coroutineContext) {
            _responseLikeComment.value = GatherDiaryRepository.INSTANCE.likeComment(commentId)
        }
    }

}