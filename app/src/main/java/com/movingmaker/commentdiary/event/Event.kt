package com.movingmaker.commentdiary.event

import android.util.Log

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class Event<out T>(private val content: T) {

    companion object{
        const val CLICK_INTERVAL = 600L
    }

    private var lastClickTime =0L

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            Log.d("what", "heeeeeeere: $hasBeenHandled")
            null
        }
        else {
            hasBeenHandled = true
            Log.d("what", "here?: $hasBeenHandled")
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}