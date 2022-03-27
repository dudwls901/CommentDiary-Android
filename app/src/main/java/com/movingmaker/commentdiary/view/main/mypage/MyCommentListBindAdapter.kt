package com.movingmaker.commentdiary.view.main.mypage

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.commentdiary.model.entity.Comment

object MyCommentListBindAdapter {
    @BindingAdapter("items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: LiveData<List<Comment>>){
        if(recyclerView.adapter==null){
            val adapter = MyCommentListAdapter()
            adapter.setHasStableIds(true)
            recyclerView.adapter = adapter
        }
        val diaryListAdapter = recyclerView.adapter as MyCommentListAdapter
            diaryListAdapter.submitList(items.value)

//        Log.d("setcomment","${diaryListAdapter.itemCount}")
    }
}