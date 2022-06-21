package com.movingmaker.commentdiary.view.main.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.commentdiary.databinding.RvItemMypageCommentBinding
import com.movingmaker.commentdiary.data.model.Comment

class MyCommentListAdapter: ListAdapter<Comment,MyCommentListAdapter.ItemViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyCommentListAdapter.ItemViewHolder = ItemViewHolder(RvItemMypageCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: MyCommentListAdapter.ItemViewHolder, position: Int) {
        holder.bind(currentList[position])

    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemId(position: Int): Long {
        return currentList[position].id
    }
    inner class ItemViewHolder(private val binding: RvItemMypageCommentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(comment : Comment){
            binding.comment = comment
        }
    }

    companion object{
        val diffUtil = object: DiffUtil.ItemCallback<Comment>(){
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem==newItem
            }

        }
    }

}