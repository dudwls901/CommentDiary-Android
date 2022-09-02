package com.movingmaker.commentdiary.util

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.internal.StringResourceValueReader
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.data.model.Comment
import com.movingmaker.commentdiary.global.CodaApplication
import com.movingmaker.commentdiary.util.Constant.EMAIL
import com.movingmaker.commentdiary.util.Constant.KAKAO
import com.movingmaker.commentdiary.view.main.mypage.MyCommentListAdapter
import java.lang.Exception

object BindingAdapter {

    @BindingAdapter("items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: LiveData<List<Comment>>) {
        if (recyclerView.adapter == null) {
            val adapter = MyCommentListAdapter()
            adapter.setHasStableIds(true)
            recyclerView.adapter = adapter
        }
        val diaryListAdapter = recyclerView.adapter as MyCommentListAdapter
        diaryListAdapter.submitList(items.value)
    }

    @BindingAdapter("android:signUpProgress")
    @JvmStatic
    fun signUpProgress(view: ProgressBar, currentFragment: LiveData<FRAGMENT_NAME>) {
        when (currentFragment.value) {
            FRAGMENT_NAME.SIGNUP_TERMS -> {
                view.visibility = View.VISIBLE
                view.progress = 25
            }
            FRAGMENT_NAME.SIGNUP_EMAIL -> {
                view.visibility = View.VISIBLE
                view.progress = 50
            }
            FRAGMENT_NAME.SIGNUP_CODE -> {
                view.visibility = View.VISIBLE
                view.progress = 75
            }
            FRAGMENT_NAME.SIGNUP_PASSWORD -> {
                view.visibility = View.VISIBLE
                view.progress = 100
            }
            else -> {
                view.visibility = View.GONE
            }
        }
    }

    @BindingAdapter("android:checkState")
    @JvmStatic
    fun setCheckState(view: ImageView, checkState: LiveData<Boolean>) {
        when (checkState.value) {
            true -> {
                view.setImageResource(R.drawable.ic_check_green)
            }
            else -> {
                view.setImageResource(R.drawable.ic_check)
            }
        }
    }

    @BindingAdapter("isCorrectEmail", "noticeEmail")
    @JvmStatic
    fun changeEmailNotice(view: TextView, isCorrect: LiveData<Boolean>, text: LiveData<String>) {
        when (isCorrect.value) {
            false -> {
                view.text = view.context.getString(R.string.onboarding_email_incorrect)
            }
            else -> {
                view.text = ""
            }
        }
        if (text.value != "" && text.value != null)
            view.text = text.value
    }

    @BindingAdapter("android:validateFindPasswordEmail")
    @JvmStatic
    fun changeFindPasswordEmailNotice(view: TextView, isCorrect: LiveData<Boolean>) {
        when (isCorrect.value) {
            false -> {
                view.visibility = View.VISIBLE
                view.text = view.context.getString(R.string.onboarding_email_incorrect)
            }
            else -> {
                view.text = ""
                view.visibility = View.GONE
            }
        }
    }

    @BindingAdapter("android:visibleEmail")
    @JvmStatic
    fun changeVisible(view: TextView, text: LiveData<String>) {
        when (text.value) {
            KAKAO -> {
                view.visibility = View.GONE
            }
            EMAIL -> {
                view.visibility = View.VISIBLE
            }
        }
    }

    @BindingAdapter("activity", "loading")
    @JvmStatic
    fun setNonClickableLoading(view: ProgressBar, activity: Activity, state: LiveData<Boolean>) {
        when (state.value) {
            true -> {
                activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                view.visibility = View.VISIBLE
            }
            else -> {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                view.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("android:yearMonth")
    @JvmStatic
    fun changeYearMonth(view: TextView, text: LiveData<String>) {
        Log.d(TAG, "changeYearMonth: onviewcreated ${text.value}")
        try {
            val (y,m) = text.value!!.split('.')
            view.text = "${y}년 ${m}월"
        }catch (e: Exception){
            view.text = "년 월"
            Log.e(TAG, "changeYearMonth: $e", )
        }
    }

    @BindingAdapter("android:buttonState")
    @JvmStatic
    fun changeButtonState(view: AppCompatButton, currentFragment: LiveData<FRAGMENT_NAME>) {
        when (currentFragment.value) {
            FRAGMENT_NAME.LOGIN_BEFORE -> {
                view.visibility = View.GONE
            }
            else -> {
                view.visibility = View.VISIBLE
                when (currentFragment.value) {
                    FRAGMENT_NAME.LOGIN, FRAGMENT_NAME.LOGIN_BEFORE -> {
                        view.text = view.context.getString(R.string.onboarding_login)
                    }
                    FRAGMENT_NAME.FIND_PASSWORD -> {
                        view.text = view.context.getString(R.string.onboarding_find_password)
                    }
                    FRAGMENT_NAME.SIGNUP_TERMS ->{
                        view.text = view.context.getString(R.string.onboarding_sign_up)
                    }
                    FRAGMENT_NAME.SIGNUP_PASSWORD -> {
                        view.text = view.context.getString(R.string.onboarding_coda_start)
                    }
                    FRAGMENT_NAME.KAKAO_TERMS ->{
                        view.text = view.context.getString(R.string.onboarding_coda_start)
                    }
                    FRAGMENT_NAME.SIGNUP_EMAIL -> {
                        view.text = view.context.getString(R.string.onboarding_send_auth)
                    }
                    FRAGMENT_NAME.SIGNUP_CODE -> {
                        view.text = view.context.getString(R.string.onboarding_certify)
                    }
                }

            }
        }
    }

/*    object CommentListBindAdapter {
    @BindingAdapter("items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: LiveData<List<Comment>>){
        if(recyclerView.adapter==null){
            val adapter = CommentListAdapter()
            adapter.setHasStableIds(true)
            recyclerView.adapter = adapter
        }
        val commentListAdapter = recyclerView.adapter as CommentListAdapter
        commentListAdapter.submitList(items.value)
    }
}*/
    /* object DiaryListBindAdapter {
     @BindingAdapter("app:items")
     @JvmStatic
     fun setItems(recyclerView: RecyclerView, items: LiveData<List<Diary>>){
         if(recyclerView.adapter==null){
             val adapter = DiaryListAdapter()
             adapter.setHasStableIds(true)
             recyclerView.adapter = adapter
         }
         val diaryListAdapter = recyclerView.adapter as DiaryListAdapter
         diaryListAdapter.submitList(items.value)
     }
 }*/
}