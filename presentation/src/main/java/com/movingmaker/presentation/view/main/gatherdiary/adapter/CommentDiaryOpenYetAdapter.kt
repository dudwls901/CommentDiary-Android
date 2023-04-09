package com.movingmaker.presentation.view.main.gatherdiary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.presentation.databinding.RvItemCommentDiaryOpenYetBinding
import com.movingmaker.presentation.view.main.gatherdiary.CommentDiaryDetailViewHolders

class CommentDiaryOpenYetAdapter :
    ListAdapter<Diary, CommentDiaryDetailViewHolders.CommentDiaryOpenYetViewHolder>(
        diffUtil
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentDiaryDetailViewHolders.CommentDiaryOpenYetViewHolder {
        return CommentDiaryDetailViewHolders.CommentDiaryOpenYetViewHolder(
            RvItemCommentDiaryOpenYetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CommentDiaryDetailViewHolders.CommentDiaryOpenYetViewHolder,
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