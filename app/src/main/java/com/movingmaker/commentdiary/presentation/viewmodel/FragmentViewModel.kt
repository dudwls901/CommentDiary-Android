package com.movingmaker.commentdiary.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movingmaker.commentdiary.presentation.util.FRAGMENT_NAME

class FragmentViewModel : ViewModel() {
    private var _curFragment = MutableLiveData<FRAGMENT_NAME>()
    val curFragment: LiveData<FRAGMENT_NAME>
        get() = _curFragment

    fun setCurrentFragment(fragment: FRAGMENT_NAME) {
        _curFragment.value = fragment
        when (fragment) {
            FRAGMENT_NAME.CALENDAR_WITH_DIARY -> {
                setHasBottomNavi(true)
            }
            FRAGMENT_NAME.RECEIVED_DIARY -> {
                setHasBottomNavi(true)
            }
            FRAGMENT_NAME.GATHER_DIARY -> {
                setHasBottomNavi(true)
            }
            FRAGMENT_NAME.MY_PAGE -> {
                setHasBottomNavi(true)
            }
            FRAGMENT_NAME.WRITE_DIARY -> {
                setHasBottomNavi(false)
            }
            FRAGMENT_NAME.COMMENT_DIARY_DETAIL -> {
                setHasBottomNavi(false)
            }
            FRAGMENT_NAME.MY_ACCOUNT -> {
                setHasBottomNavi(false)
            }
            FRAGMENT_NAME.TERMS -> {
                setHasBottomNavi(false)
            }
            FRAGMENT_NAME.SENDED_COMMENT_LIST -> {
                setHasBottomNavi(false)
            }
            FRAGMENT_NAME.PUSHALARM_ONOFF -> {
                setHasBottomNavi(false)
            }
            FRAGMENT_NAME.ALONE_DIARY_DETAIL -> {
                setHasBottomNavi(false)
            }
            else -> setHasBottomNavi(false)
        }

    }

    private var _hasBottomNavi = MutableLiveData<Boolean>()


    val hasBottomNavi: LiveData<Boolean>
        get() = _hasBottomNavi

    private fun setHasBottomNavi(bool: Boolean) {
        _hasBottomNavi.value = bool
    }

}