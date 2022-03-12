package com.movingmaker.commentdiary.viewmodel.mydiary

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.movingmaker.commentdiary.model.local.entity.LocalDiary
import com.movingmaker.commentdiary.model.local.repository.LocalDiaryRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocalDiaryViewModel(application: Application): AndroidViewModel(application) {
    private val repository = LocalDiaryRespository(application)
    private val _localDiaryList = MutableLiveData<List<LocalDiary>>()

    val localDiaryList: LiveData<List<LocalDiary>>
        get() = _localDiaryList

    init {
        _localDiaryList.value = emptyList()
    }

    fun saveDiary(localDiary: LocalDiary){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(localDiary)
        }
    }

    fun deleteDiary(localDiary: LocalDiary){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(localDiary)
        }
    }

    fun editDiary(localDiary: LocalDiary){
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(localDiary)
        }
    }

    fun getAll(){
        viewModelScope.launch {
            var diaryList: List<LocalDiary> = emptyList()
            withContext(Dispatchers.IO) {
                diaryList = repository.getAll()
            }
            Log.d("localDiaryViewmodel", "getAll: ${diaryList}")
            _localDiaryList.value = diaryList
            Log.d("localDiaryViewmodel", "getAll: ${localDiaryList.value}")
        }
    }




}