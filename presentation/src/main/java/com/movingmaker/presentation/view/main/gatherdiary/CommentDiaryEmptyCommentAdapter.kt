package com.movingmaker.presentation.view.main.gatherdiary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.presentation.databinding.RvItemCommentDiaryEmptyCommentBinding

class CommentDiaryEmptyCommentAdapter() :
    ListAdapter<Diary, CommentDiaryDetailViewHolders.CommentDiaryEmptyCommentViewHolder>(
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

        val diffUtil = object : DiffUtil.ItemCallback<Diary>() {
            override fun areItemsTheSame(oldItem: Diary, newItem: Diary) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Diary, newItem: Diary) =
                oldItem.hashCode() == newItem.hashCode()
        }
    }
}