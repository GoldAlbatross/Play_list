package com.practicum.playlistmaker.features.player.data

import android.app.Application
import android.media.MediaPlayer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.features.itunes_api.data.network.InternetController
import com.practicum.playlistmaker.features.player.domain.api.Player
import com.practicum.playlistmaker.screens.player.model.PlayerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class PlayerImpl(
    private val appContext: Application,
    private val mediaPlayer: MediaPlayer,
    private val netChecker: InternetController,
): Player {

    private var state: PlayerState = PlayerState.Loading(message = appContext.getString(R.string.loading))

override fun prepareMediaPlayer(url: String): Flow<PlayerState> = callbackFlow {
    if (netChecker.isInternetAvailable()) {
        mediaPlayer.apply {
            reset()
            setOnPreparedListener { state = PlayerState.Ready; trySend(state) }
            setOnErrorListener { _,_,_ ->
                state = PlayerState.Error(message = appContext.getString(R.string.check_network))
                trySend(state)
                true
            }
            setDataSource(url)
            prepare()
        }
    } else {
        state = PlayerState.Error(message = appContext.getString(R.string.check_network))
        trySend(state)
    }
    awaitClose {
        mediaPlayer.setOnPreparedListener(null)
        mediaPlayer.setOnErrorListener(null)
    }
}.flowOn(Dispatchers.IO)

    override fun runTrack() {
        state = PlayerState.Play
        mediaPlayer.start()
    }

    override fun getTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setStopListener(): Flow<PlayerState> = callbackFlow {
        mediaPlayer.setOnCompletionListener{
            state = PlayerState.Ready
            trySend(state)
        }
        awaitClose {
            mediaPlayer.setOnCompletionListener(null)
        }
    }

    override fun stopTrack() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            state = PlayerState.Pause
        }
    }

    override fun stop() {
        if (state is PlayerState.Pause) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
        state = PlayerState.Loading(message = appContext.getString(R.string.loading))
    }

    override fun getState(): PlayerState = state

}