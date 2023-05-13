package com.practicum.playlistmaker.utils

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import java.text.SimpleDateFormat
import java.util.Locale


fun <T : Parcelable?> Intent.getParcelableFromIntent(key: String, clazz: Class<T>): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getParcelableExtra(key, clazz)!!
    else {
        @Suppress("DEPRECATION")
        getParcelableExtra<T>(key)!!
    }
}

fun <T : Parcelable?> Bundle.getParcelableFromBundle(key: String, clazz: Class<T>): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getParcelable(key, clazz)!!
    else {
        @Suppress("DEPRECATION")
        getParcelable<T>(key)!!
    }
}

fun View.debounceClickListener(debouncer: Debouncer, listenerBlock: () -> Unit) {
    setOnClickListener { debouncer.onClick(listenerBlock) }
}

fun Int.getTimeFormat():String {
    return SimpleDateFormat("mm:ss", Locale.US).format(this)
}
