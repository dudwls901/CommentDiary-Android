package com.movingmaker.commentdiary.viewmodel.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.movingmaker.commentdiary.R

class IntroViewModel(application: Application) : AndroidViewModel(application) {
    private var _introImage = MutableLiveData<Int>()

    val introImage: LiveData<Int>
        get() = _introImage

    init{

    }

    fun setIntroPageNum(idx: Int){
        when(idx){
            0->{
                _introImage.value = R.drawable.img_onboarding_1
            }
            1->{
                _introImage.value = R.drawable.img_onboarding_2
            }
            2->{
                _introImage.value = R.drawable.img_onboarding_3
            }
        }
    }

}