package com.movingmaker.presentation.util

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
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

@BindingAdapter("checkState")
fun bindCheckState(view: ImageView, checkState: Boolean) {
    when (checkState) {
        true -> view.setImageResource(R.drawable.ic_check_green)
        else -> view.setImageResource(R.drawable.ic_check)
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

@BindingAdapter("popBackStack")
fun bindPopBackStack(view: View, dummy: Any?) {
    view.setOnClickListener {
        view.findNavController().popBackStack()
    }
}