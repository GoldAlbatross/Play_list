package com.practicum.playlistmaker._player.presentation

import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.models.domain.Track
import com.practicum.playlistmaker.tools.KEY_TRACK
import com.practicum.playlistmaker.tools.getParcelableFromIntent

class PlayerRouter(private val activity: AppCompatActivity) {

    fun getTrackFromIntent(): Track =
        activity.intent.getParcelableFromIntent(KEY_TRACK, Track::class.java)

    fun goBack() { activity.finish() }
}