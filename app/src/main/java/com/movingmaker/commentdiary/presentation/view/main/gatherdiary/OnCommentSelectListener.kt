package com.movingmaker.commentdiary.presentation.view.main.gatherdiary

interface OnCommentSelectListener {
    fun onHeartClickListener(commentId: Long)
    fun onReportClickListener(commentId: Long)
    fun onBlockClickLinstener(commentId: Long)
}