package com.movingmaker.presentation.view.main.mydiary

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.presentation.R
import com.movingmaker.presentation.util.DIARY_TYPE
import com.movingmaker.presentation.util.getCodaToday
import com.movingmaker.presentation.util.getSplitYMD
import com.movingmaker.presentation.util.ymdToDate

@BindingAdapter("app:writeDiaryPreviewHead")
fun TextView.bindWriteDiaryPreviewHead(date: LiveData<String>) {
    date.value?.let {
        val (_, m, d) = it.getSplitYMD()
        text = context.getString(R.string.diary_head_date_text, m, d)
    }
}

@BindingAdapter("app:diaryPreviewHead")
fun TextView.bindDiaryPreviewHeadText(diary: LiveData<Diary?>) {
    diary.value?.let {
        this.setTextColor(
            if (it.deliveryYN == 'Y') {
                context.getColor(R.color.core_green)
            } else {
                context.getColor(R.color.core_orange)
            }
        )
        val (_, m, d) = it.date.getSplitYMD()
        text = context.getString(R.string.diary_head_date_text, m, d)
    }
}

@BindingAdapter("diaryHint")
fun EditText.bindDiaryHint(diaryType: LiveData<DIARY_TYPE>) {
    hint = when (diaryType.value) {
        DIARY_TYPE.COMMENT_DIARY -> context.getString(R.string.write_comment_diary_content_hint)
        else -> context.getString(R.string.write_alone_diary_content_hint)
    }
}

@BindingAdapter("colorWithDiaryType")
fun TextView.bindColorWithDiaryType(diaryType: LiveData<DIARY_TYPE>) {
    setTextColor(
        when (diaryType.value) {
            DIARY_TYPE.COMMENT_DIARY -> context.getColor(R.color.core_green)
            else -> context.getColor(R.color.core_orange)
        }
    )
}

@BindingAdapter("sendButtonState")
fun AppCompatButton.bindSendButtonState(canSendCommentDiary: LiveData<Boolean>) {
    background = when (canSendCommentDiary.value) {
        true -> AppCompatResources.getDrawable(context, R.drawable.background_green_radius_10)
        else -> AppCompatResources.getDrawable(
            context,
            R.drawable.background_green_alpha_40_radius_10
        )
    }
}

@BindingAdapter("app:diaryType")
fun TextView.bindVisibleWriteCommentDiaryNotice(diaryType: LiveData<DIARY_TYPE>) {
    visibility = if (diaryType.value == DIARY_TYPE.COMMENT_DIARY) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("diaryType", "isExpand", "selectedDate")
fun bindVisibleWithDiaryType(
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
                else -> {}
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
                    (view as TextView)
                    tempSelectedDiaryDate.value?.let {
                        val selectedDiaryDate = ymdToDate(it)
                        selectedDiaryDate?.let { selectedDate ->
                            if (selectedDate < getCodaToday()) {
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
