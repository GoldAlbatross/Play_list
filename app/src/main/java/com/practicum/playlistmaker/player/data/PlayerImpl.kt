package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.model.PlayerStates
import com.practicum.playlistmaker.player.domain.api.Player

class PlayerImpl:
    Player {

    private val mediaPlayer = MediaPlayer()
    override var state = PlayerStates.DEFAULT

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
}