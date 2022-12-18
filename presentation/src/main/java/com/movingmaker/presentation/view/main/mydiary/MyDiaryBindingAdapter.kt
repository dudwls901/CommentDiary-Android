package com.movingmaker.presentation.view.main.mydiary

import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.movingmaker.presentation.R
import com.movingmaker.presentation.util.DIARY_TYPE


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