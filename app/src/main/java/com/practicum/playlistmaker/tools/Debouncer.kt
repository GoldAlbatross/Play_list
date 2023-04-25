package com.practicum.playlistmaker.tools

import android.os.Handler
import android.os.Looper


class Debouncer(
    private val handler: Handler = Handler(Looper.getMainLooper()),
){
    private var available = true
    fun onClick(listener: () -> Unit) {
        if (available) {
            available = false
            handler.postDelayed({ available = true }, DELAY_500)
            listener.invoke()
        } else return
    }
}




