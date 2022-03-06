package com.movingmaker.commentdiary.viewmodel.mydiary

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.model.entity.Comment
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
    private var _selectedDiary = MutableLiveData<Diary>()
    private var _dateDiaryText = MutableLiveData<String>()
    private var _diaryType = MutableLiveData<Int>()
    private var _commentDiaryTextCount = MutableLiveData<Int>()
    private var _curDateDiaryState = MutableLiveData<Int>()
    private var _saveOrEdit = MutableLiveData<String>()

    val diaryTypeMap =  HashMap<Int,String>()
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

    val selectedDiary: LiveData<Diary>
        get() = _selectedDiary

    val dateDiaryText: LiveData<String>
        get() = _dateDiaryText

    val diaryType: LiveData<Int>
        get() = _diaryType

    val commentDiaryTextCount: LiveData<Int>
        get() = _commentDiaryTextCount

    val saveOrEdit: LiveData<String>
        get() = _saveOrEdit

    init {
        _aloneDiary.value = emptyList()
        _commentDiary.value = emptyList()
        _monthDiaries.value = emptyList()
        _selectedDiary.value = Diary(0L,"","","",' ', null)
        _dateDiaryText.value = ""
        _diaryType.value = -1
        _commentDiaryTextCount.value = 0
        diaryTypeMap[0] = "aloneDiarySave"
        diaryTypeMap[1] = "commentDiarySave"
        diaryTypeMap[2] = "aloneDiaryEdit"
        diaryTypeMap[3] = "commentDiaryEdit"
        _saveOrEdit.value = ""
    }

    private fun setAloneDiary(list: List<CalendarDay>) {
        Log.d("mydiaryviewmodel", "setMonthDiaries: ${list.size}")
        _aloneDiary.value = list
    }

    private fun setCommentDiary(list: List<CalendarDay>) {
        Log.d("mydiaryviewmodel", "setMonthDiaries: ${list.size}")
        _commentDiary.value = list
    }

    fun setDateDiaryText(text: String){
        _dateDiaryText.value= text
    }

    fun setSelectedDiary(diary: Diary){
        _selectedDiary.value = diary
    }

    fun setMonthDiaries(list: List<Diary>) {
        _monthDiaries.value = list

        val aloneDiary = ArrayList<CalendarDay>()
        val commentDiary = ArrayList<CalendarDay>()

        for (selectedDiary in list) {
            val ymd = selectedDiary.date
            val (year, month, day) = ymd.split('.').map { it.toInt() }
            if (selectedDiary.deliveryYN == 'Y') {
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

    fun setDiaryType(type: Int){
        _diaryType.value = type
    }

    fun setCommentDiaryTextCount(count: Int){
        _commentDiaryTextCount.value = count
    }

    fun setSaveOrEdit(state: String){
        _saveOrEdit.value = state
    }

    suspend fun setResponseGetMonthDiary(date: String) {
        withContext(viewModelScope.coroutineContext) {
            _responseGetMonthDiary.value = MyDiaryRepository.INSTANCE.getMonthDiary(date)
        }
    }
}