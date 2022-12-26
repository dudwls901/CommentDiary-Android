package com.movingmaker.presentation.view.main.gatherdiary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.presentation.databinding.RvItemCommentDiaryCommentYetBinding

class CommentDiaryCommentYetAdapter() :
    RecyclerView.Adapter<CommentDiaryDetailViewHolders.CommentDiaryCommentYetViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentDiaryDetailViewHolders.CommentDiaryCommentYetViewHolder {
        return CommentDiaryDetailViewHolders.CommentDiaryCommentYetViewHolder(
            RvItemCommentDiaryCommentYetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CommentDiaryDetailViewHolders.CommentDiaryCommentYetViewHolder,
        position: Int
    ) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 1
    }

//    fun setData(userName: String, repositoryName: String) {
//        Log.d(TAG, "setData: ")
//        headerText = "$userName / $repositoryName"
//    }
}