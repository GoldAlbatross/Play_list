package com.practicum.playlistmaker.tools

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable


fun <T : Parcelable?> Intent.getParcelable(key: String, clazz: Class<T>): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        this.getParcelableExtra(key, clazz)!!
    else {
        @Suppress("DEPRECATION")
        this.getParcelableExtra<T>(key)!!
    }
}


fun <T : Parcelable?> Bundle.getParcelableFromBundle(key: String, clazz: Class<T>): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        this.getParcelable(key, clazz)!!
    else {
        @Suppress("DEPRECATION")
        this.getParcelable<T>(key)!!
    }
}