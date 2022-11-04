package com.movingmaker.commentdiary.presentation.view.main.gatherdiary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.commentdiary.data.model.Comment
import com.movingmaker.commentdiary.databinding.RvItemMydiaryCommentBinding
import timber.log.Timber

class CommentListAdapter(private val onCommentSelectListener: OnCommentSelectListener) :
    ListAdapter<Comment, CommentListAdapter.ItemViewHolder>(
        diffUtil
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder = ItemViewHolder(
        RvItemMydiaryCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ), onCommentSelectListener
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemId(position: Int): Long {
        return currentList[position].id
    }

    class ItemViewHolder(
        private val binding: RvItemMydiaryCommentBinding,
        val onCommentSelectListener: OnCommentSelectListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.comment = comment
            //todo 하트 애니메이션
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

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                Timber.d("areContentsTheSame: $oldItem $newItem")
                return oldItem.like == newItem.like &&
                        oldItem.content == newItem.content &&
                        oldItem.date == newItem.date
            }

        }
    }

}