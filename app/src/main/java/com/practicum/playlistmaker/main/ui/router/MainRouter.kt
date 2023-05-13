package com.practicum.playlistmaker.main.ui.router

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.media_lib.ui.MediaLibActivity
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.ui.activity.SettingsActivity

class MainRouter(private val context: Context) {

    fun openSearchActivity() {
        val intent = Intent(context, SearchActivity::class.java)
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun openMediaLibActivity() {
        val intent = Intent(context, MediaLibActivity::class.java)
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun openSettingsActivity() {
        val intent = Intent(context, SettingsActivity::class.java)
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}