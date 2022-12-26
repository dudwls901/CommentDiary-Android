package com.movingmaker.presentation.view.main.gatherdiary

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.presentation.databinding.RvItemCommentDiaryBinding
import com.movingmaker.presentation.databinding.RvItemCommentDiaryCommentYetBinding
import com.movingmaker.presentation.databinding.RvItemCommentDiaryEmptyCommentBinding
import com.movingmaker.presentation.databinding.RvItemCommentDiaryOpenYetBinding

sealed class CommentDiaryDetailViewHolders(binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    class CommentDiaryContentViewHolder(private val binding: RvItemCommentDiaryBinding) :
        CommentDiaryDetailViewHolders(binding) {

        fun bind(selectedDiary: Diary) {
            binding.selectedDiary = selectedDiary
        }
    }

    class CommentDiaryOpenYetViewHolder(private val binding: RvItemCommentDiaryOpenYetBinding) :
        CommentDiaryDetailViewHolders(binding) {
        fun bind() {
//            binding.issueContentTextView
        }
    }

    class CommentDiaryEmptyCommentViewHolder(private val binding: RvItemCommentDiaryEmptyCommentBinding) :
        CommentDiaryDetailViewHolders(binding) {
        fun bind() {

        }
    }

    class CommentDiaryCommentYetViewHolder(private val binding: RvItemCommentDiaryCommentYetBinding) :
        CommentDiaryDetailViewHolders(binding) {
        fun bind() {

        }
    }
}
