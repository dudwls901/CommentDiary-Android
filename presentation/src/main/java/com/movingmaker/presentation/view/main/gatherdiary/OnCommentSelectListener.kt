package com.movingmaker.presentation.view.main.gatherdiary

interface OnCommentSelectListener {
    fun onHeartClickListener(commentId: Long)
    fun onReportClickListener(commentId: Long)
    fun onBlockClickListener(commentId: Long)
}