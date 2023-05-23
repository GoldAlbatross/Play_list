package com.practicum.playlistmaker.features.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.features.player.domain.model.PlayerStates
import com.practicum.playlistmaker.features.player.domain.api.Player

class PlayerImpl(private val mediaPlayer: MediaPlayer):
    Player {

    private var state = PlayerStates.DEFAULT

    override fun prepareMediaPlayer(url: String, listener: () -> Unit) {
        mediaPlayer.apply {
            setDataSource(url)
            setOnPreparedListener { state = PlayerStates.PREPARED; listener.invoke() }
            prepareAsync()
        }
    }

    override fun runTrack() {
        state = PlayerStates.PLAYING
        mediaPlayer.start()
    }

    override fun getTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setStopListener(listener: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            state = PlayerStates.PREPARED
            listener.invoke()
        }
    }

    override fun stopTrack() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            state = PlayerStates.PAUSED
        }
    }

    override fun stop() {
        mediaPlayer.release()
    }

    override fun getState(): PlayerStates {
        return state
    }
}