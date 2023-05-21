package com.practicum.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.model.PlayerStates
import com.practicum.playlistmaker.player.ui.model.AddButtonModel
import com.practicum.playlistmaker.player.ui.model.LikeButtonModel
import com.practicum.playlistmaker.player.ui.model.PlayButtonState
import com.practicum.playlistmaker.utils.DELAY_300
import com.practicum.playlistmaker.utils.SingleLiveEvent

class PlayerViewModel(val interactor: PlayerInteractor): ViewModel() {


    private val handler = Handler(Looper.getMainLooper())
    private val playButtonState = SingleLiveEvent<PlayButtonState>()
    private val likeButtonState = SingleLiveEvent<LikeButtonModel>()
    private val addButtonState = SingleLiveEvent<AddButtonModel>()
    private val timeState = MutableLiveData<String>()

    fun playButtonStateLiveData(): LiveData<PlayButtonState> = playButtonState
    fun likeButtonStateLiveData(): LiveData<LikeButtonModel> = likeButtonState
    fun addButtonStateLiveData(): LiveData<AddButtonModel> = addButtonState
    fun timeStateLiveData(): LiveData<String> = timeState

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
        interactor.stopMediaPlayer()
    }

    fun preparePlayer(trackLink: String) {
        playButtonState.postValue(PlayButtonState.Prepare(clicked = false))
        interactor.prepareMediaPlayer(trackLink) {
            playButtonState.postValue(PlayButtonState.PrepareDone)
        }
        interactor.setStopListenerOnMediaPlayer {
            playButtonState.postValue(PlayButtonState.PrepareDone)
            handler.removeCallbacksAndMessages(null)
        }
    }

    fun onClickLike() {
        likeButtonState.postValue(LikeButtonModel.Like)
    }

    fun onClickAdd() {
        addButtonState.postValue(AddButtonModel.Add)
    }

    fun onClickedPlay() {
        when (interactor.getState()) {
            PlayerStates.DEFAULT -> playButtonState.postValue(PlayButtonState.Prepare(clicked = true))
            PlayerStates.PREPARED -> playMusic()
            PlayerStates.PLAYING -> pauseMusic()
            PlayerStates.PAUSED -> playMusic()
        }
    }

    private fun playMusic() {
        playButtonState.postValue(PlayButtonState.Play)
        interactor.runPlayer()
        handler.post(object : Runnable {
            override fun run() {
                timeState.postValue(interactor.getTime())
                handler.postDelayed(this, DELAY_300)
            }
        })
    }

    fun pauseMusic() {
        playButtonState.postValue(PlayButtonState.Pause)
        interactor.pauseMediaPlayer()
        handler.removeCallbacksAndMessages(null)
    }




    companion object {
        fun factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    PlayerViewModel(interactor = Creator.providePlayerInteractor())
                }
            }
    }
}