package com.movingmaker.commentdiary.presentation.util

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
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.data.model.Comment
import com.movingmaker.commentdiary.presentation.view.main.mypage.MyCommentListAdapter
import timber.log.Timber

@BindingAdapter("items")
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

@BindingAdapter("diaryType", "isExpand", "selectedDate")
fun changeVisibleWithDiaryType(
    view: View,
    diaryType: LiveData<DIARY_TYPE>,
    isExpand: LiveData<Boolean>,
    tempSelectedDiaryDate: LiveData<String>
) {
    when (isExpand.value) {
        true -> {
            when (view.id) {
                R.id.sendButton, R.id.saveButton -> {
                    view.visibility = View.INVISIBLE
                }
                R.id.selectAloneDiaryInRadioButton -> {
                    when (diaryType.value) {
                        DIARY_TYPE.COMMENT_DIARY -> {
                            view.visibility = View.GONE
                        }
                        DIARY_TYPE.ALONE_DIARY -> {
                            view.visibility = View.VISIBLE
                        }
                        else -> {}
                    }
                }
                R.id.selectCommentDiaryInRadioButton -> {
                    when (diaryType.value) {
                        DIARY_TYPE.COMMENT_DIARY -> {
                            view.visibility = View.VISIBLE
                        }
                        DIARY_TYPE.ALONE_DIARY -> {
                            view.visibility = View.GONE
                        }
                        else -> {}
                    }
                }
                else -> {
//                    (view as TextView).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
//                    view.context.getString(R.string.select_diary_type)
                }
            }
        }
        else -> {
            when (view.id) {
                R.id.sendButton -> {
                    when (diaryType.value) {
                        DIARY_TYPE.COMMENT_DIARY -> {
                            view.visibility = View.VISIBLE
                        }
                        DIARY_TYPE.ALONE_DIARY -> {
                            view.visibility = View.INVISIBLE
                        }
                        else -> {}
                    }
                }
                R.id.saveButton -> {
                    when (diaryType.value) {
                        DIARY_TYPE.COMMENT_DIARY -> {
                            view.visibility = View.INVISIBLE
                        }
                        DIARY_TYPE.ALONE_DIARY -> {
                            view.visibility = View.VISIBLE
                        }
                        else -> {}
                    }
                }
                R.id.selectCommentDiaryInRadioButton, R.id.selectAloneDiaryInRadioButton -> {
                    view.visibility = View.GONE
                }
                else -> {
//                    (view as TextView).setCompoundDrawablesWithIntrinsicBounds(
//                        0,
//                        0,
//                        R.drawable.ic_arrow_down,
//                        0
//                    )
                    (view as TextView)
                    tempSelectedDiaryDate.value?.let {
                        val selectedDiaryDate = DateConverter.ymdToDate(it)
                        if (selectedDiaryDate < DateConverter.getCodaToday()) {
                            view.isEnabled = false
                            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        } else {
                            view.isEnabled = true
                            view.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.ic_arrow_down,
                                0
                            )
                        }
                    }
                    view.text = when (diaryType.value) {
                        DIARY_TYPE.COMMENT_DIARY -> view.context.getString(R.string.diary_type_comment)
                        else -> view.context.getString(R.string.diary_type_alone)
                    }
                }
            }
        }
    }
}

@BindingAdapter("app:nonClickableActivity", "app:nonClickableLoading")
fun setNonClickableLoading(view: ProgressBar, activity: Activity, state: LiveData<Boolean>) {
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

@BindingAdapter("app:loading")
fun setLoading(view: ProgressBar, state: LiveData<Boolean>) {
    view.visibility = when (state.value) {
        true -> View.VISIBLE
        else -> View.GONE
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("android:yearMonth")
fun changeYearMonth(view: TextView, text: LiveData<String>) {
    try {
        val (y, m) = text.value!!.split('.')
        view.text = "${y}년 ${m}월"
    } catch (e: Exception) {
        view.text = "년 월"
        Timber.e("changeYearMonth: $e")
    }
}

@BindingAdapter("android:buttonState")
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
            }

        }
    }
}

/*
* write diary
* */
@BindingAdapter("app:colorWithDiaryType")
fun TextView.setColorWithDiaryType(diaryType: LiveData<DIARY_TYPE>) {
    setTextColor(
        when (diaryType.value) {
            DIARY_TYPE.COMMENT_DIARY -> context.getColor(R.color.core_green)
            else -> context.getColor(R.color.core_orange)
        }
    )
}

@BindingAdapter("app:sendButtonState")
fun AppCompatButton.setSendButtonState(canSendCommentDiary: LiveData<Boolean>) {
    background = when (canSendCommentDiary.value) {
        true -> context.getDrawable(R.drawable.background_green_radius_10)
        else -> context.getDrawable(R.drawable.background_green_alpha_40_radius_10)
    }
}

@BindingAdapter("app:navigateUp")
fun navigateUp(view: View, dummy: Any?) {
    view.setOnClickListener {
        view.findNavController().navigateUp()
    }
}