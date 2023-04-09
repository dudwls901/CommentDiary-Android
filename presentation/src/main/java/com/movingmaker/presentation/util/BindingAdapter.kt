package com.movingmaker.presentation.util

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.presentation.R
import com.movingmaker.presentation.view.main.mypage.MyCommentListAdapter
import timber.log.Timber
//todo bindingadapter 분리

@BindingAdapter("items")
fun bindItems(recyclerView: RecyclerView, items: LiveData<List<Comment>>) {
    if (recyclerView.adapter == null) {
        val adapter = MyCommentListAdapter()
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }
    val diaryListAdapter = recyclerView.adapter as MyCommentListAdapter
    diaryListAdapter.submitList(items.value)
}

@BindingAdapter("signUpProgress")
fun bindSignUpProgress(view: ProgressBar, currentFragment: LiveData<FRAGMENT_NAME>) {
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

@BindingAdapter("checkState")
fun bindCheckState(view: ImageView, checkState: LiveData<Boolean>) {
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
fun bindEmailNotice(view: TextView, isCorrect: LiveData<Boolean>, text: LiveData<String>) {
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

@BindingAdapter("validateFindPasswordEmail")
fun bindFindPasswordEmailNotice(view: TextView, isCorrect: LiveData<Boolean>) {
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

@BindingAdapter("visibleEmail")
fun bindVisible(view: TextView, text: LiveData<String>) {
    when (text.value) {
        KAKAO -> {
            view.visibility = View.GONE
        }
        EMAIL -> {
            view.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("nonClickableActivity", "nonClickableLoading")
fun bindNonClickableLoading(view: ProgressBar, activity: Activity, state: LiveData<Boolean>) {
    when (state.value) {
        true -> {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            view.visibility = View.VISIBLE
        }
        else -> {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            view.visibility = View.GONE
        }
    }
}

@BindingAdapter("loading")
fun bindLoading(view: ProgressBar, state: LiveData<Boolean>) {
    view.visibility = when (state.value) {
        true -> View.VISIBLE
        else -> View.GONE
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("yearMonth")
fun bindYearMonth(view: TextView, text: LiveData<String>) {
    try {
        val (y, m) = text.value!!.split('.')
        view.text = "${y}년 ${m}월"
    } catch (e: Exception) {
        view.text = "년 월"
        Timber.e("changeYearMonth: $e")
    }
}

@BindingAdapter("buttonState")
fun bindButtonState(view: AppCompatButton, currentFragment: LiveData<FRAGMENT_NAME>) {
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
                FRAGMENT_NAME.SIGNUP_TERMS -> {
                    view.text = view.context.getString(R.string.onboarding_sign_up)
                }
                FRAGMENT_NAME.SIGNUP_PASSWORD -> {
                    view.text = view.context.getString(R.string.onboarding_coda_start)
                }
                FRAGMENT_NAME.KAKAO_TERMS -> {
                    view.text = view.context.getString(R.string.onboarding_coda_start)
                }
                FRAGMENT_NAME.SIGNUP_EMAIL -> {
                    view.text = view.context.getString(R.string.onboarding_send_auth)
                }
                FRAGMENT_NAME.SIGNUP_CODE -> {
                    view.text = view.context.getString(R.string.onboarding_certify)
                }
                else -> {}
            }

        }
    }
}

@BindingAdapter("popBackStack")
fun bindPopBackStack(view: View, dummy: Any?) {
    view.setOnClickListener {
        view.findNavController().popBackStack()
    }
}