package com.movingmaker.commentdiary.viewmodel.mydiary

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
import com.movingmaker.commentdiary.model.remote.response.CommentListResponse
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.SaveDiaryResponse
import com.movingmaker.commentdiary.model.repository.MyDiaryRepository
import com.movingmaker.commentdiary.model.repository.MyPageRepository
import com.movingmaker.commentdiary.util.DateConverter
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.*

class MyDiaryViewModel : ViewModel() {
    private var _aloneDiary = MutableLiveData<List<CalendarDay>>()
    private var _commentDiary = MutableLiveData<List<CalendarDay>>()
    private var _monthDiaries = MutableLiveData<List<Diary>>()
    private var _selectedDiary = MutableLiveData<Diary>()
    private var _dateDiaryText = MutableLiveData<String>()
    private var _deliveryYN = MutableLiveData<Char>()
    private var _commentDiaryTextCount = MutableLiveData<Int>()
    private var _saveOrEdit = MutableLiveData<String>()
    private var _selectedDate = MutableLiveData<String>()
    private var _commentList = MutableLiveData<List<Comment>>()
    private var _haveDayMyComment = MutableLiveData<Boolean>()

    //api response
    private var _responseGetMonthDiary = MutableLiveData<Response<DiaryListResponse>>()
    private var _responseSaveDiary = MutableLiveData<Response<SaveDiaryResponse>>()
    private var _responseEditDiary = MutableLiveData<Response<IsSuccessResponse>>()
    private var _responseDeleteDiary = MutableLiveData<Response<IsSuccessResponse>>()
    private var _responseGetDayComment = MutableLiveData<Response<CommentListResponse>>()

    val aloneDiary: LiveData<List<CalendarDay>>
        get() = _aloneDiary

    val commentDiary: LiveData<List<CalendarDay>>
        get() = _commentDiary

    val monthDiaries: LiveData<List<Diary>>
        get() = _monthDiaries

    val selectedDiary: LiveData<Diary>
        get() = _selectedDiary

    val dateDiaryText: LiveData<String>
        get() = _dateDiaryText

    val deliveryYN: LiveData<Char>
        get() = _deliveryYN

    val commentDiaryTextCount: LiveData<Int>
        get() = _commentDiaryTextCount

    val saveOrEdit: LiveData<String>
        get() = _saveOrEdit

    val selectedDate: LiveData<String>
        get() = _selectedDate

    val commentList: LiveData<List<Comment>>
        get() = _commentList

    val haveDayMyComment: LiveData<Boolean>
        get() = _haveDayMyComment


    val responseGetMonthDiary: LiveData<Response<DiaryListResponse>>
        get() = _responseGetMonthDiary

    val responseSaveDiary: LiveData<Response<SaveDiaryResponse>>
        get() = _responseSaveDiary

    val responseEditDiary: LiveData<Response<IsSuccessResponse>>
        get() = _responseEditDiary

    val responseDeleteDiary: LiveData<Response<IsSuccessResponse>>
        get() = _responseDeleteDiary

    val responseGetDayComment: LiveData<Response<CommentListResponse>>
        get() = _responseGetDayComment


    init {
//        _aloneDiary.value = emptyList()
//        _commentDiary.value = emptyList()
//        _monthDiaries.value = emptyList()
        _deliveryYN.value =' '
        _selectedDiary.value = Diary(null,"","","",' ',' ', null)
        _dateDiaryText.value = ""
        _commentDiaryTextCount.value = 0
        _saveOrEdit.value = ""
//        _selectedDate.value = DateConverter.ymdFormat(DateConverter.getCodaToday())
        _selectedDate.value = ""
    }

    private fun setAloneDiary(list: List<CalendarDay>) {
        _aloneDiary.value = list
    }

    private fun setCommentDiary(list: List<CalendarDay>) {
        _commentDiary.value = list
    }

    fun setMonthDiaries(list: List<Diary>) {

        val aloneDiary = ArrayList<CalendarDay>()
        val commentDiary = ArrayList<CalendarDay>()

        for (selectedDiary in list) {
            val ymd = selectedDiary.date
            val (year, month, day) = ymd.split('.').map { it.toInt() }
            if (selectedDiary.deliveryYN == 'Y') {
                if(selectedDiary.tempYN=='N')
                commentDiary.add(CalendarDay.from(year, month-1, day))
            } else {
                aloneDiary.add(CalendarDay.from(year, month-1, day))
            }

        }

        //그냥 실행하면 background thread로 작업돼서 컴파일 에러
        //livedata의 setValue는 백그라운드 스레드로 작업 불가
        setCommentDiary(commentDiary.toList())
        setAloneDiary(aloneDiary.toList())
        _monthDiaries.value = list
    }

    //for diary data
    fun setSelectedDiary(diary: Diary){
        _selectedDiary.value = diary
        _commentList.value = diary.commentList?: emptyList()
    }

    fun setDeliveryYN(type: Char){
//        _deliveryYN.value = type
        _selectedDiary.value!!.deliveryYN = type
    }

    fun setSelectedDate(date: String?){
        _selectedDate.value = date ?: null
//        _selectedDiary.value!!.date = date
    }
    fun setDiaryTitle(title: String){
        _selectedDiary.value!!.title = title
    }
    fun setDiaryContent(content: String){
        _selectedDiary.value!!.content = content
    }


    //for view
    fun setDateDiaryText(text: String){
        _dateDiaryText.value= text
    }

    fun setCommentDiaryTextCount(count: Int){
        _commentDiaryTextCount.value = count
    }

    fun setSaveOrEdit(state: String){
        _saveOrEdit.value = state
    }

    fun setHaveDayMyComment(isHave: Boolean){
        _haveDayMyComment.value = isHave
    }

    fun deleteLocalReportedComment(commentId: Long){
        //신고한 코멘트 삭
        for(idx in _selectedDiary.value!!.commentList!!.indices){
            val comment = selectedDiary.value!!.commentList!![idx]
            if(comment.id == commentId){
                _selectedDiary.value!!.commentList!!.removeAt(idx)
                return
            }
        }
    }

    fun likeLocalComment(commentId: Long){
        //신고한 코멘트 삭
        for(idx in _selectedDiary.value!!.commentList!!.indices){
            val comment = selectedDiary.value!!.commentList!![idx]
            if(comment.id == commentId){
                _selectedDiary.value!!.commentList!![idx].like=true
                return
            }
        }
    }


    suspend fun setResponseGetMonthDiary(date: String) {
        withContext(viewModelScope.coroutineContext) {
            _responseGetMonthDiary.value = MyDiaryRepository.INSTANCE.getMonthDiary(date)
        }
    }

    suspend fun setResponseSaveDiary(saveDiaryRequest: SaveDiaryRequest) {
        withContext(viewModelScope.coroutineContext) {
            _responseSaveDiary.value = MyDiaryRepository.INSTANCE.saveDiary(saveDiaryRequest)
        }
    }

    suspend fun setResponseEditDiary(diaryId: Long,editDiaryRequest: EditDiaryRequest) {
        withContext(viewModelScope.coroutineContext) {
            _responseEditDiary.value = MyDiaryRepository.INSTANCE.editDiary(diaryId,editDiaryRequest)
        }
    }

    suspend fun setResponseDeleteDiary(diaryId: Long){
        withContext(viewModelScope.coroutineContext){
            _responseDeleteDiary.value = MyDiaryRepository.INSTANCE.deleteDiary(diaryId)
        }
    }

    suspend fun setResponseGetDayComment(date: String){
        withContext(viewModelScope.coroutineContext){
            _responseGetDayComment.value = MyPageRepository.INSTANCE.getMonthComment(date)
        }
    }

}