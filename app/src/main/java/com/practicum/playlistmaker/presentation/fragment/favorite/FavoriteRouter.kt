package com.practicum.playlistmaker.presentation.fragment.favorite

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.presentation.activity.player.PlayerActivity
import com.practicum.playlistmaker.utils.KEY_TRACK

class FavoriteRouter(private val activity: Context) {

    fun openPlayerActivity(track: Track) {
        Intent(activity, PlayerActivity::class.java).apply {
            putExtra(KEY_TRACK, track)
            activity.startActivity(this)
        }
    }
}