package com.practicum.playlistmaker.screens.search.router

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.screens.player.activity.PlayerActivity
import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.utils.KEY_TRACK

class SearchRouter(private val activity: Context) {

    fun openPlayerActivity(track: Track) {
        Intent(activity, PlayerActivity::class.java).apply {
            putExtra(KEY_TRACK, track)
            activity.startActivity(this)
        }
    }
}