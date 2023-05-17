package com.practicum.playlistmaker.search.ui.router

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.utils.KEY_TRACK

class SearchRouter(private val activity: AppCompatActivity) {

    fun openPlayerActivity(track: Track) {
        Intent(activity, PlayerActivity::class.java).apply {
            putExtra(KEY_TRACK, track)
            activity.startActivity(this)
        }
    }
    fun goBack() { activity.finish() }
}