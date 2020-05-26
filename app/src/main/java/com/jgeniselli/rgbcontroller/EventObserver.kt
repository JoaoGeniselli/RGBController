package com.jgeniselli.rgbcontroller

import androidx.lifecycle.Observer

class EventObserver<T>(private val onEventChanged: (T) -> Unit) : Observer<Event<T>> {

    override fun onChanged(t: Event<T>?) {
        t?.getContentIfNotHandled()?.let { content ->
            onEventChanged(content)
        }
    }
}