package com.practicum.playlistmaker.screens.media_lib.router

import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.utils.KEY_TRACK
import com.practicum.playlistmaker.utils.getParcelableFromIntent

class MLRouter(
    private val activity: AppCompatActivity,
) {

    fun getTrackFromIntent(): Track {
        return activity.intent.getParcelableFromIntent(KEY_TRACK, Track::class.java)
    }

    fun onClickedBack() { activity.finish() }
}