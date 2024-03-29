package com.movingmaker.presentation.view.main.gatherdiary

import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.presentation.databinding.RvItemCommentDiaryBinding
import com.movingmaker.presentation.databinding.RvItemCommentDiaryCommentYetBinding
import com.movingmaker.presentation.databinding.RvItemCommentDiaryEmptyCommentBinding
import com.movingmaker.presentation.databinding.RvItemCommentDiaryOpenYetBinding
import com.movingmaker.presentation.databinding.RvItemMydiaryCommentBinding

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

        init {
            binding.goToWriteCommentButton.setOnClickListener {
                binding.root.findNavController()
                    .navigate(CommentDiaryDetailFragmentDirections.actionCommentDiaryDetailFragmentToReceivedDiaryFragment())
            }
        }
        fun bind(selectedDiary: Diary) {
            binding.selectedDiary = selectedDiary
        }
    }

    class CommentDiaryEmptyCommentViewHolder(private val binding: RvItemCommentDiaryEmptyCommentBinding) :
        CommentDiaryDetailViewHolders(binding) {
        fun bind(message: String) {
            binding.message = message
        }
    }

    class CommentDiaryCommentYetViewHolder(private val binding: RvItemCommentDiaryCommentYetBinding) :
        CommentDiaryDetailViewHolders(binding) {
        fun bind(selectedDiary: Diary) {
            binding.selectedDiary = selectedDiary
        }
    }

    class CommentViewHolder(
        private val binding: RvItemMydiaryCommentBinding,
        private val onCommentSelectListener: OnCommentSelectListener
    ) : CommentDiaryDetailViewHolders(binding) {
        fun bind(comment: Comment) {
            binding.comment = comment
            binding.commentHeartImageView.setOnClickListener {
//                if(!comment.like) {
                onCommentSelectListener.onHeartClickListener(comment.id)
//                    binding.commentHeartImageView.setImageResource(R.drawable.ic_heart_fill)
//                }
            }
            binding.commentReportTextView.setOnClickListener {
                onCommentSelectListener.onReportClickListener(comment.id)
            }
            binding.commentBlockTextView.setOnClickListener {
                onCommentSelectListener.onBlockClickListener(comment.id)
            }
        }
    }
}
