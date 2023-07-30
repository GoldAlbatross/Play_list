package com.practicum.playlistmaker.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Debouncer(
    private val coroutineScope: CoroutineScope,
    private val delay: Long = DELAY_1000,
){

    var job: Job? = null
    private var available = true

    fun onClick(action: () -> Unit) {
        Log.d("qqq", "onClick")
        if (available) {
            available = false
            job = coroutineScope.launch {
                Log.d("qqq", "available = $available")
                action()
                delay(delay)
                available = true
                Log.d("qqq", "available = $available")
            }
        }
    }
}

fun <T> debounce(
    delayMillis: Long = DELAY_1000,
    coroutineScope: CoroutineScope,
    deferredUsing: Boolean = false,
    action: (T) -> Unit,
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (deferredUsing) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || deferredUsing) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}




