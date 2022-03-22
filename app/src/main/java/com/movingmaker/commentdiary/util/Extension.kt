package com.movingmaker.commentdiary.util

import android.content.res.Resources

object Extension {
    fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
    fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
}