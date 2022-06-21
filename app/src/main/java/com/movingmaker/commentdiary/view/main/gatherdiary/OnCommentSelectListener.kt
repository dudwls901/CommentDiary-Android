package com.movingmaker.commentdiary.view.main.gatherdiary

interface OnCommentSelectListener {
    fun onHeartClickListener(commentId: Long)
    fun onReportClickListener(commentId: Long)
    fun onBlockClickLinstener(commentId: Long)
}