package com.practicum.playlistmaker.domain.player_interactor

import com.practicum.playlistmaker.domain.model.PlayerStates
import com.practicum.playlistmaker.domain.repository.IMediaPlayerRepository

class PlayerInteractor(
    private val mediaPlayer: IMediaPlayerRepository
): IPlayerInteractor {

    override fun prepareMediaPlayer(url: String, listener: () -> Unit) {
        mediaPlayer.prepareMediaPlayer(url) {listener.invoke()}
    }

    override fun getState(): PlayerStates {
        return mediaPlayer.state
    }

    override fun runPlayer() {
        mediaPlayer.runTrack()
    }

    override fun getTime(): Int {
        return mediaPlayer.getTime()
    }

    override fun setStopListenerOnMediaPlayer(listener: () -> Unit) {
        mediaPlayer.setStopListener {listener.invoke()}
    }

    override fun pauseMediaPlayer() {
        mediaPlayer.stopTrack()
    }

    override fun stopMediaPlayer() {
        mediaPlayer.stop()
    }

}