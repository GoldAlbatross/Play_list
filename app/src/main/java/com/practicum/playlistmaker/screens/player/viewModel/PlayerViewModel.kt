package com.practicum.playlistmaker.screens.player.viewModel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.features.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.screens.player.model.AddButtonModel
import com.practicum.playlistmaker.screens.player.model.LikeButtonModel
import com.practicum.playlistmaker.screens.player.model.PlayButtonState
import com.practicum.playlistmaker.utils.DELAY_300
import com.practicum.playlistmaker.utils.SingleLiveEvent

class PlayerViewModel(
    val interactor: PlayerInteractor
): ViewModel()  {


    private val handler = Handler(Looper.getMainLooper())
    private val playButtonState = SingleLiveEvent<PlayButtonState>()
    private val likeButtonState = SingleLiveEvent<LikeButtonModel>()
    private val addButtonState = SingleLiveEvent<AddButtonModel>()
    private val timeState = MutableLiveData<String>()
    private var prepareListener: (() -> Unit)? = {
        playButtonState.postValue(interactor.getState())
    }
    private var completionListener: (() -> Unit)? = {
        playButtonState.postValue(PlayButtonState.PrepareDone)
        handler.removeCallbacksAndMessages(null)
    }

    fun playButtonStateLiveData(): LiveData<PlayButtonState> = playButtonState
    fun likeButtonStateLiveData(): LiveData<LikeButtonModel> = likeButtonState
    fun addButtonStateLiveData(): LiveData<AddButtonModel> = addButtonState
    fun timeStateLiveData(): LiveData<String> = timeState

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
        interactor.stopMediaPlayer()
        prepareListener = null
        completionListener = null
    }

    fun preparePlayer(trackLink: String) {
        playButtonState.value = PlayButtonState.Loading()
        interactor.prepareMediaPlayer(trackLink) { prepareListener?.invoke() }
        interactor.setStopListenerOnMediaPlayer { completionListener?.invoke() }
    }

    fun onClickLike() {
        likeButtonState.postValue(LikeButtonModel.Like)
    }

    fun onClickAdd() {
        addButtonState.postValue(AddButtonModel.Add)
    }

    fun onClickedPlay() {
        when (val state = interactor.getState()) {
            is PlayButtonState.Loading -> playButtonState.postValue(state)
            is PlayButtonState.Error -> playButtonState.postValue(state)
            is PlayButtonState.PrepareDone -> playMusic()
            is PlayButtonState.Play -> pauseMusic()
            is PlayButtonState.Pause -> playMusic()
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
}