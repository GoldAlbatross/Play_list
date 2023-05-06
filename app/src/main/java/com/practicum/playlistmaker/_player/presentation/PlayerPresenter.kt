package com.practicum.playlistmaker._player.presentation

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker._player.domain.model.PlayerStates
import com.practicum.playlistmaker._player.domain.interactor._PlayerInteractor
import com.practicum.playlistmaker.tools.DELAY_300

class PlayerPresenter(
    private val view: _PlayerView,
    private val interactor: _PlayerInteractor,
    private val router: PlayerRouter,
    ) {

    private val track = router.getTrackFromIntent()
    private val handler = Handler(Looper.getMainLooper())
    init {
        interactor.prepareMediaPlayer(track.previewUrl) { view.startAnimationAlfa() }
        interactor.setStopListenerOnMediaPlayer {
            view.changeImageForPlayButton(R.drawable.player_play)
            handler.removeCallbacksAndMessages(null)
        }
        view.drawScreen(track = track)
    }

    fun onClickedBack() { router.goBack() }

    fun onClickedPlay() {
        when (interactor.getState()) {
            PlayerStates.DEFAULT -> view.showToast()
            PlayerStates.PREPARED -> playMusic()
            PlayerStates.PLAYING -> pauseMusic()
            PlayerStates.PAUSED -> playMusic()
        }
        view.startAnimationScale()
    }

    private fun playMusic() {
        interactor.runPlayer()
        view.changeImageForPlayButton(R.drawable.player_pause)
        handler.post(object : Runnable {
            override fun run() {
                view.updateTime(interactor.getTime())
                handler.postDelayed(this, DELAY_300)
            }
        })
    }

    fun pauseMusic() {
        interactor.pauseMediaPlayer()
        view.changeImageForPlayButton(R.drawable.player_play)
        handler.removeCallbacksAndMessages(null)
    }

    fun stopMediaPlayer() {
        handler.removeCallbacksAndMessages(null)
        interactor.stopMediaPlayer()
    }
}