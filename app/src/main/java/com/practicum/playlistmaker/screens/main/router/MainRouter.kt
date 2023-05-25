package com.practicum.playlistmaker.screens.main.router

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.screens.media_lib.activity.MediaLibActivity
import com.practicum.playlistmaker.screens.search.activity.SearchActivity
import com.practicum.playlistmaker.screens.settings.activity.SettingsActivity

class MainRouter(private val activity: AppCompatActivity) {

    fun onClickedSearch() {
        activity.startActivity(Intent(activity, SearchActivity::class.java))
    }

    fun onClickedMediaLib() {
        activity.startActivity(Intent(activity, MediaLibActivity::class.java))
    }

    fun onClickedSettings() {
        activity.startActivity(Intent(activity, SettingsActivity::class.java))
    }
}