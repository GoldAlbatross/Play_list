package com.practicum.playlistmaker.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Debouncer(
    private val coroutineScope: CoroutineScope,
    private val delay: Long = DELAY_1000,
){
    private var job: Job? = null
    private var available = true

    fun onClick(action: () -> Unit) {
        if (available) {
            available = false
            job = coroutineScope.launch {
                action()
                delay(delay)
                available = true
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




