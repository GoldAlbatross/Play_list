package com.practicum.playlistmaker.screens.player.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.features.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.features.storage.local_db.domain.api.AlbumInteractor
import com.practicum.playlistmaker.features.storage.local_db.domain.api.FavoriteInteractor
import com.practicum.playlistmaker.features.storage.local_db.domain.model.ScreenState
import com.practicum.playlistmaker.screens.player.model.PlayerState
import com.practicum.playlistmaker.utils.DELAY_300
import com.practicum.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val interactor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val albumInteractor: AlbumInteractor,
): ViewModel()  {

    private val playButtonState = SingleLiveEvent<PlayerState>()
    private val likeButtonState = SingleLiveEvent<Boolean>()
    private val timeState = MutableLiveData<String>()

    private var timerJob: Job? = null
    private var playerJob: Job? = null

    fun playButtonStateLiveData(): LiveData<PlayerState> = playButtonState
    fun likeButtonStateLiveData(): LiveData<Boolean> = likeButtonState
    fun timeStateLiveData(): LiveData<String> = timeState

    private val addButtonState = MutableStateFlow<ScreenState>(ScreenState.Empty)
    val addButtonStateFlow: StateFlow<ScreenState> = addButtonState.asStateFlow()

    override fun onCleared() {
        super.onCleared()
        interactor.stopMediaPlayer()
        timerJob = null
        playerJob = null
    }

    fun preparePlayer(trackLink: String) {
        playButtonState.value = PlayerState.Loading()
        playerJob = viewModelScope.launch {
            interactor.prepareMediaPlayer(trackLink).collect { state ->
                playButtonState.value = state
            }
        }

    }

    fun getFavoriteState(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteInteractor.isFavorite(id)
                .take(1)
                .collect{ likeButtonState.postValue(it) }
        }
    }

    fun onClickLike(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteInteractor.isFavorite(track.trackId).collect { favorite ->
                if (favorite)
                    favoriteInteractor.removeFromFavorite(track.trackId)
                else
                    favoriteInteractor.addToFavorite(track)
                likeButtonState.postValue(!favorite)
            }
        }
    }

    fun onClickAdd() {
        viewModelScope.launch(Dispatchers.IO) {
            albumInteractor.getAlbumlist().collect { list ->
                if (list.isEmpty()) addButtonState.emit(ScreenState.Empty)
                else addButtonState.emit(ScreenState.Content(list))
            }
        }
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