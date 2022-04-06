package com.movingmaker.commentdiary.view.main.gatherdiary

import com.movingmaker.commentdiary.model.entity.Diary

interface OnCommentSelectListener {
    fun onHeartClickListener(commentId: Long)
    fun onReportClickListener(commentId: Long)
    fun onBlockClickLinstener(commentId: Long)
}