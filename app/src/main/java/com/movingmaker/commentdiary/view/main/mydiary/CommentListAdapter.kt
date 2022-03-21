package com.movingmaker.commentdiary.view.main.mydiary

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.commentdiary.databinding.RvItemMydiaryCommentBinding
import com.movingmaker.commentdiary.model.entity.Comment
import com.prolificinteractive.materialcalendarview.CalendarDay

class CommentListAdapter: ListAdapter<Comment,CommentListAdapter.ItemViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentListAdapter.ItemViewHolder = ItemViewHolder(RvItemMydiaryCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: CommentListAdapter.ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    inner class ItemViewHolder(private val binding: RvItemMydiaryCommentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(comment : Comment){
            binding.comment = comment
            binding.commentHeartImageView.setOnClickListener {

            }
        }
    }

    companion object{
        val diffUtil = object: DiffUtil.ItemCallback<Comment>(){
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }

        }
    }

}