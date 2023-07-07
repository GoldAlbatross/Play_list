package com.practicum.playlistmaker.features.player.data

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.features.player.domain.api.Player
import com.practicum.playlistmaker.screens.player.model.PlayButtonState
import java.io.IOException

class PlayerImpl(
    private val appContext: Application,
    private val mediaPlayer: MediaPlayer
): Player {

    private var state: PlayButtonState = PlayButtonState.Loading(message = appContext.getString(R.string.loading))
    override fun prepareMediaPlayer(url: String, listener: () -> Unit) {
        if (isInternetAvailable()) {
            try {
                mediaPlayer.apply {
                    setOnPreparedListener { state = PlayButtonState.PrepareDone; listener.invoke() }
                    mediaPlayer.reset()
                    setDataSource(url)
                    prepareAsync()
                }
            } catch (e: IOException) {
                state = PlayButtonState.Error(message = appContext.getString(R.string.check_network)); listener.invoke()
            }
        } else { state = PlayButtonState.Error(message = appContext.getString(R.string.check_network)); listener.invoke() }
    }

    override fun runTrack() {
        state = PlayButtonState.Play
        mediaPlayer.start()
    }

    override fun getTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setStopListener(listener: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            state = PlayButtonState.PrepareDone
            listener.invoke()
        }
    }

    override fun stopTrack() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            state = PlayButtonState.Pause
        }
    }

    override fun stop() {
        mediaPlayer.stop()
        mediaPlayer.reset()
    }

    override fun getState(): PlayButtonState = state


    private fun isInternetAvailable(): Boolean {
        val connectivityManager = appContext.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}