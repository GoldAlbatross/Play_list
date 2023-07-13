package com.practicum.playlistmaker.screens.player.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.screens.player.model.AddButtonModel
import com.practicum.playlistmaker.screens.player.model.LikeButtonModel
import com.practicum.playlistmaker.screens.player.model.PlayerState
import com.practicum.playlistmaker.utils.DELAY_300
import com.practicum.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val interactor: PlayerInteractor
): ViewModel()  {

    private val playButtonState = SingleLiveEvent<PlayerState>()
    private val likeButtonState = SingleLiveEvent<LikeButtonModel>()
    private val addButtonState = SingleLiveEvent<AddButtonModel>()
    private val timeState = MutableLiveData<String>()

    private var timerJob: Job? = null
    private var playerJob: Job? = null

    fun playButtonStateLiveData(): LiveData<PlayerState> = playButtonState
    fun likeButtonStateLiveData(): LiveData<LikeButtonModel> = likeButtonState
    fun addButtonStateLiveData(): LiveData<AddButtonModel> = addButtonState
    fun timeStateLiveData(): LiveData<String> = timeState

    override fun onCleared() {
        super.onCleared()
        interactor.stopMediaPlayer()
    }

    fun preparePlayer(trackLink: String) {
        playButtonState.value = PlayerState.Loading()
        playerJob = viewModelScope.launch {
            interactor.prepareMediaPlayer(trackLink).collect { state ->
                playButtonState.value = state
            }
        }

    }

    fun onClickLike() {
        likeButtonState.postValue(LikeButtonModel.Like)
    }

    fun onClickAdd() {
        addButtonState.postValue(AddButtonModel.Add)
    }

    fun onClickedPlay() {
        when (val state = interactor.getState()) {
            is PlayerState.Loading -> {
                playButtonState.value = state
            }
            is PlayerState.Error -> {
                playButtonState.value = state
            }
            is PlayerState.Ready -> {
                playMusic()
                setListener()
            }
            is PlayerState.Play -> {
                pauseMusic()
            }
            is PlayerState.Pause -> {
                playMusic()
            }
        }
    }

    private fun setListener() {
        viewModelScope.launch {
            interactor.setStopListenerOnMediaPlayer().collect { state ->
                playButtonState.value = state
            }
        }
    }

    private fun playMusic() {
        interactor.runPlayer()
        playButtonState.postValue(PlayerState.Play)
        timerJob = viewModelScope.launch {
            do {
                timeState.value = interactor.getTime()
                delay(DELAY_300)
            } while (interactor.getState() is PlayerState.Play)
        }
    }

    fun pauseMusic() {
        timerJob?.cancel()
        interactor.pauseMediaPlayer()
        playButtonState.value = interactor.getState()
    }
}