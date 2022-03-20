package com.movingmaker.commentdiary.view.main.mypage

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.commentdiary.model.entity.Comment
import com.movingmaker.commentdiary.model.entity.Diary

object CommentListBindAdapter {
    @BindingAdapter("items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: LiveData<List<Comment>>){
        if(recyclerView.adapter==null){
            val adapter = CommentListAdapter()
            adapter.setHasStableIds(true)
            recyclerView.adapter = adapter
        }
        val diaryListAdapter = recyclerView.adapter as CommentListAdapter
        diaryListAdapter.submitList(items.value)
    }
}