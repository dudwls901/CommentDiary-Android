package com.movingmaker.commentdiary.util
import androidx.lifecycle.Observer

class EventObserver<T>(private val onEventUnHandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let{ value->
            onEventUnHandledContent(value)
        }
    }
}