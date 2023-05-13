package com.practicum.playlistmaker.search.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.models.domain.Track
import com.practicum.playlistmaker.utils.KEY_TRACK

class SearchRouter(private val activity: AppCompatActivity) {

    fun openPlayerActivity(track: Track) {
        val intent = Intent(activity, PlayerActivity::class.java)
        intent.putExtra(KEY_TRACK, track)
        activity.startActivity(intent)
    }
}