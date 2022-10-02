package com.movingmaker.commentdiary.presentation.viewmodel.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movingmaker.commentdiary.R

class IntroViewModel : ViewModel() {
    private var _introImage = MutableLiveData<Int>()

    val introImage: LiveData<Int>
        get() = _introImage

    fun setIntroPageNum(idx: Int) {
        when (idx) {
            0 -> {
                _introImage.value = R.drawable.img_onboarding_1
            }
            1 -> {
                _introImage.value = R.drawable.img_onboarding_2
            }
            2 -> {
                _introImage.value = R.drawable.img_onboarding_3
            }
        }
    }

}