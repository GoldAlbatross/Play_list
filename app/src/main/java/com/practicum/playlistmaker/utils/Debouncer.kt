package com.practicum.playlistmaker.utils

import android.os.Handler
import android.os.Looper


class Debouncer(
    private val handler: Handler = Handler(Looper.getMainLooper()),
){
    private var available = true
    fun onClick(listener: () -> Unit) {
        if (available) {
            available = false
            handler.postDelayed({ available = true }, DELAY_1000)
            listener.invoke()
        }
    }
}




