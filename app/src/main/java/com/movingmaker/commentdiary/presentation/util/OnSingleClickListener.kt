package com.movingmaker.commentdiary.presentation.util

import android.view.View

class OnSingleClickListener(private val onSingleClick: (View) -> Unit): View.OnClickListener {

    companion object{
        private const val CLICK_INTERVAL = 1000L
    }
    private var lastClickTime = 0L

    override fun onClick(v: View) {
        val currentClickTime = System.currentTimeMillis()
        val elapsedTime = currentClickTime-lastClickTime
        lastClickTime = currentClickTime
        if(elapsedTime > CLICK_INTERVAL){
            onSingleClick(v)
        }
    }
}
fun View.setOnSingleClickListener(block: (View) -> Unit){
    val listener = OnSingleClickListener{
        block(it)
    }
    setOnClickListener(listener)
}