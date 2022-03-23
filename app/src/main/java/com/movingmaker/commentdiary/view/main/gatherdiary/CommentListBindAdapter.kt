package com.movingmaker.commentdiary.view.main.gatherdiary//package com.movingmaker.commentdiary.view.main.mydiary
//
//import android.util.Log
//import androidx.databinding.BindingAdapter
//import androidx.lifecycle.LiveData
//import androidx.recyclerview.widget.RecyclerView
//import com.movingmaker.commentdiary.model.entity.Comment
//
//object CommentListBindAdapter {
//    @BindingAdapter("items")
//    @JvmStatic
//    fun setItems(recyclerView: RecyclerView, items: LiveData<List<Comment>>){
//        if(recyclerView.adapter==null){
//            val adapter = CommentListAdapter()
//            adapter.setHasStableIds(true)
//            recyclerView.adapter = adapter
//        }
//        val commentListAdapter = recyclerView.adapter as CommentListAdapter
//        commentListAdapter.submitList(items.value)
//    }
//}