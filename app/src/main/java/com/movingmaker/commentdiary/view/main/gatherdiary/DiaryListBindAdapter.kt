package com.movingmaker.commentdiary.view.main.gatherdiary

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.commentdiary.model.entity.Diary

//object DiaryListBindAdapter {
//    @BindingAdapter("app:items")
//    @JvmStatic
//    fun setItems(recyclerView: RecyclerView, items: LiveData<List<Diary>>){
//        if(recyclerView.adapter==null){
//            val adapter = DiaryListAdapter()
//            adapter.setHasStableIds(true)
//            recyclerView.adapter = adapter
//        }
//        val diaryListAdapter = recyclerView.adapter as DiaryListAdapter
//        diaryListAdapter.submitList(items.value)
//    }
//}