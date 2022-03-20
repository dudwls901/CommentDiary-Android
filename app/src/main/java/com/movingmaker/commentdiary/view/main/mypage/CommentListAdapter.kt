package com.movingmaker.commentdiary.view.main.mypage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.commentdiary.databinding.RvItemGatherdiaryDiaryBinding
import com.movingmaker.commentdiary.databinding.RvItemMypageCommentBinding
import com.movingmaker.commentdiary.model.entity.Comment
import com.movingmaker.commentdiary.model.entity.Diary
import com.movingmaker.commentdiary.util.CustomTag.ATAG

class CommentListAdapter(): ListAdapter<Comment,CommentListAdapter.ItemViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentListAdapter.ItemViewHolder = ItemViewHolder(RvItemMypageCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: CommentListAdapter.ItemViewHolder, position: Int) {
        holder.bind(currentList[position])

    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    inner class ItemViewHolder(private val binding: RvItemMypageCommentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(comment : Comment){
            binding.comment = comment
        }
    }

    companion object{
        val diffUtil = object: DiffUtil.ItemCallback<Comment>(){
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem==newItem
            }

        }
    }

}