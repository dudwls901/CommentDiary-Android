package com.movingmaker.commentdiary.util

import android.content.res.Resources

object Extension {
    @JvmStatic fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
    @JvmStatic fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
}