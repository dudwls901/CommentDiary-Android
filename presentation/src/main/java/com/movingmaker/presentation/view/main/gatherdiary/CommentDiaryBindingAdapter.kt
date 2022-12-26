package com.movingmaker.presentation.view.main.gatherdiary

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.presentation.R
import com.movingmaker.presentation.util.getCodaToday
import com.movingmaker.presentation.util.ymdToDate

@BindingAdapter("app:emptyCommentNotice")
fun TextView.bindEmptyCommentNotice(selectedDiary: Diary) {
    //todo 임시저장 일기인 경우 코멘트 전송 시간이 지났어요, 임시저장 아닌 경우 아쉽게도 코메트가 도착하지 않았어요.
    text = this.context.getString(R.string.empty_comment)
//    R.string.pass_diary_send_time
}

@BindingAdapter("app:commentYetState")
fun View.bindCommentYetButtonState(selectedDiary: Diary) {
    ymdToDate(selectedDiary.date)?.let { diaryDate ->
        handleCommentYetButton(
            this,
            diaryDate <= getCodaToday().minusDays(2)
        )
    }
}

private fun handleCommentYetButton(view: View, isLater: Boolean) = with(view) {
    if (isLater) {
        visibility = when (this) {
            is ImageButton -> {
                View.VISIBLE
            }
            else -> {
                View.GONE
            }
        }
    } else {
        visibility = when (this) {
            is ImageButton -> {
                View.GONE
            }
            else -> {
                View.VISIBLE
            }
        }
    }
}

@BindingAdapter("app:openYetCommentNotice")
fun View.bindOpenYetCommentNotice(selectedDiary: Diary) {
    ymdToDate(selectedDiary.date)?.let { diaryDate ->
        handleOpenYetCommentNotice(
            this,
            diaryDate <= getCodaToday().minusDays(2)
        )
    }
}

private fun handleOpenYetCommentNotice(view: View, isLater: Boolean) = with(view) {
    if (isLater) {
        when (this) {
            is Button -> {
                visibility = View.GONE
            }
            is TextView -> {
                text = this.context.getString(R.string.no_write_comment)
            }
        }
    } else {
        when (this) {
            is Button -> {
                visibility = View.VISIBLE
            }
            is TextView -> {
                text = this.context.getString(R.string.yet_write_comment)
            }
        }
    }
}