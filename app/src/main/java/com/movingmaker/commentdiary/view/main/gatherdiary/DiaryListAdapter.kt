package com.movingmaker.commentdiary.view.main.gatherdiary

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.commentdiary.databinding.RvItemGatherdiaryDiaryBinding
import com.movingmaker.commentdiary.model.entity.Diary
import com.movingmaker.commentdiary.util.CustomTag.ATAG

class DiaryListAdapter(val onDiarySelectListener: OnDiarySelectListener): ListAdapter<Diary,DiaryListAdapter.ItemViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiaryListAdapter.ItemViewHolder = ItemViewHolder(RvItemGatherdiaryDiaryBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: DiaryListAdapter.ItemViewHolder, position: Int) {
        holder.bind(currentList[position])

    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    inner class ItemViewHolder(private val binding: RvItemGatherdiaryDiaryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(diary : Diary){
            binding.diary = diary
            binding.root.setOnClickListener {
                onDiarySelectListener.onDiarySelectListener(diary)
            }
        }
    }

    companion object{
        val diffUtil = object: DiffUtil.ItemCallback<Diary>(){
            override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
                return oldItem == newItem
            }


        }
    }

}