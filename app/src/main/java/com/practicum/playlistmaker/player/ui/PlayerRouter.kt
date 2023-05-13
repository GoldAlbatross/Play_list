package com.practicum.playlistmaker.player.ui

import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.models.domain.Track
import com.practicum.playlistmaker.utils.KEY_TRACK
import com.practicum.playlistmaker.utils.getParcelableFromIntent

class PlayerRouter(private val activity: AppCompatActivity) {

    fun getTrackFromIntent(): Track =
        activity.intent.getParcelableFromIntent(KEY_TRACK, Track::class.java)

    fun goBack() { activity.finish() }
}