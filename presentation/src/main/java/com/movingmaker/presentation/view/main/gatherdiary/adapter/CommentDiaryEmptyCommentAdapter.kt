package com.movingmaker.presentation.view.main.gatherdiary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.movingmaker.presentation.databinding.RvItemCommentDiaryEmptyCommentBinding
import com.movingmaker.presentation.view.main.gatherdiary.CommentDiaryDetailViewHolders

class CommentDiaryEmptyCommentAdapter() :
    ListAdapter<String, CommentDiaryDetailViewHolders.CommentDiaryEmptyCommentViewHolder>(
        diffUtil
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentDiaryDetailViewHolders.CommentDiaryEmptyCommentViewHolder {
        return CommentDiaryDetailViewHolders.CommentDiaryEmptyCommentViewHolder(
            RvItemCommentDiaryEmptyCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CommentDiaryDetailViewHolders.CommentDiaryEmptyCommentViewHolder,
        position: Int
    ) {
        holder.bind(currentList[position])
    }

    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) =
                oldItem.hashCode() == newItem.hashCode()

            override fun areContentsTheSame(oldItem: String, newItem: String) =
                oldItem.hashCode() == newItem.hashCode()
        }
    }
}