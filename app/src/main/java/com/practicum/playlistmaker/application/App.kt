package com.practicum.playlistmaker.application

import android.app.Application
import android.content.SharedPreferences
import com.google.gson.Gson

class App : Application() {


    internal lateinit var sharedPreference: SharedPreferences
        private set
    internal lateinit var gson: Gson
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        gson = Gson()
        sharedPreference = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
    }

    companion object {
        private const val APP_PREFERENCES = "app_preferences"
        lateinit var instance: App
            private set
    }
}