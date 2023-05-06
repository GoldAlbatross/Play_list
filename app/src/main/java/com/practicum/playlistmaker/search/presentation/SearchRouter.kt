package com.practicum.playlistmaker.search.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.player.presentation.PlayerActivity
import com.practicum.playlistmaker.models.domain.Track
import com.practicum.playlistmaker.tools.KEY_TRACK

class SearchRouter(private val activity: AppCompatActivity) {

    fun openPlayerActivity(track: Track) {
        val intent = Intent(activity, PlayerActivity::class.java)
        intent.putExtra(KEY_TRACK, track)
        activity.startActivity(intent)
    }
}