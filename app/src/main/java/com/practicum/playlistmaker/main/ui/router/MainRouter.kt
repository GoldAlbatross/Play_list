package com.practicum.playlistmaker.main.ui.router

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.library.ui.activity.MediaLibActivity
import com.practicum.playlistmaker.search.ui.activity.SearchActivity
import com.practicum.playlistmaker.settings.ui.activity.SettingsActivity

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