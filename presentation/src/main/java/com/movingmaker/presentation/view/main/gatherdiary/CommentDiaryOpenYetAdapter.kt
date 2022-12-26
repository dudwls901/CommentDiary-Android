package com.movingmaker.presentation.view.main.gatherdiary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.presentation.databinding.RvItemCommentDiaryOpenYetBinding

class CommentDiaryOpenYetAdapter() :
    RecyclerView.Adapter<CommentDiaryDetailViewHolders.CommentDiaryOpenYetViewHolder>() {

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