package com.practicum.playlistmaker.presentation.activity.player

import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.utils.KEY_TRACK
import com.practicum.playlistmaker.utils.getParcelableFromIntent

class PlayerRouter(private val activity: AppCompatActivity) {


    fun getTrackFromIntent(): Track {
        return activity.intent.getParcelableFromIntent(KEY_TRACK, Track::class.java)
    }

    fun onClickedBack() { activity.finish() }
}