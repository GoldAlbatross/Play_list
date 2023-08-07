package com.practicum.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.domain.player.api.PlayerInteractor
import com.practicum.playlistmaker.domain.local_db.api.PlayListInteractor
import com.practicum.playlistmaker.domain.local_db.api.FavoriteInteractor
import com.practicum.playlistmaker.domain.local_db.model.Album
import com.practicum.playlistmaker.domain.local_db.model.BottomSheetUIState
import com.practicum.playlistmaker.presentation.activity.player.PlayerState
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
import kotlinx.coroutines.withContext

class PlayerViewModel(
    private val interactor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val playListInteractor: PlayListInteractor,
): ViewModel()  {

    private val playButtonState = SingleLiveEvent<PlayerState>()
    private val likeButtonState = SingleLiveEvent<Boolean>()
    private val timeState = MutableLiveData<String>()

    private var timerJob: Job? = null

    fun playButtonStateLiveData(): LiveData<PlayerState> = playButtonState
    fun likeButtonStateLiveData(): LiveData<Boolean> = likeButtonState
    fun timeStateLiveData(): LiveData<String> = timeState

    private val addButtonState = MutableStateFlow<BottomSheetUIState>(BottomSheetUIState.Empty)
    val addButtonStateFlow: StateFlow<BottomSheetUIState> = addButtonState.asStateFlow()

    override fun onCleared() {
        super.onCleared()
        interactor.stopMediaPlayer()
    }

    fun preparePlayer(trackLink: String) {
        playButtonState.value = PlayerState.Loading()
        viewModelScope.launch {
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
            playListInteractor.getAlbumList().collect { list ->
                if (list.isEmpty()) addButtonState.emit(BottomSheetUIState.Empty)
                else addButtonState.emit(BottomSheetUIState.Content(list))
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

    fun onClickedAlbum(track: Track, album: Album) {
        viewModelScope.launch {
            val song = album.trackList.filter { it.trackId == track.trackId }
            if (song.isNotEmpty()) addButtonState.emit(BottomSheetUIState.Exist(album.name))
            else {
                withContext(Dispatchers.IO) {
                    playListInteractor.addToAlbum(track, album)
                    addButtonState.emit(BottomSheetUIState.Success(album.name))
                }
            }
        }
    }
}