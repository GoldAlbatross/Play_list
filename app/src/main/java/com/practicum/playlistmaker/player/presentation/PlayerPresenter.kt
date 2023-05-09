package com.practicum.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.models.domain.PlayerStates
import com.practicum.playlistmaker.player.domain.interactor.PlayerInteractor
import com.practicum.playlistmaker.tools.DELAY_300

class PlayerPresenter(
    private val view: PlayerView,
    private val interactor: PlayerInteractor,
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