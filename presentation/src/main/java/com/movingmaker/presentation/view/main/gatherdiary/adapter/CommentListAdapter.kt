package com.movingmaker.presentation.view.main.gatherdiary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.presentation.databinding.RvItemMydiaryCommentBinding
import com.movingmaker.presentation.view.main.gatherdiary.CommentDiaryDetailViewHolders
import com.movingmaker.presentation.view.main.gatherdiary.OnCommentSelectListener

class CommentListAdapter(private val onCommentSelectListener: OnCommentSelectListener) :
    ListAdapter<Comment, CommentDiaryDetailViewHolders.CommentViewHolder>(
        diffUtil
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentDiaryDetailViewHolders.CommentViewHolder =
        CommentDiaryDetailViewHolders.CommentViewHolder(
            RvItemMydiaryCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onCommentSelectListener
        )

    override fun onBindViewHolder(
        holder: CommentDiaryDetailViewHolders.CommentViewHolder,
        position: Int
    ) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemId(position: Int): Long {
        return currentList[position].id
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.like == newItem.like &&
                        oldItem.content == newItem.content &&
                        oldItem.date == newItem.date
            }

        }
    }

}