package com.movingmaker.commentdiary.util

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.commentdiary.data.model.Comment
import com.movingmaker.commentdiary.view.main.mypage.MyCommentListAdapter

object BindingAdapter {

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