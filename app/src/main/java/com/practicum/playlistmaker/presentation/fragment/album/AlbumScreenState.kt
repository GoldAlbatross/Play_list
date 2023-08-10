package com.practicum.playlistmaker.presentation.fragment.album

import com.practicum.playlistmaker.domain.local_db.model.Album

sealed interface AlbumScreenState {
    object Default: AlbumScreenState
    data class EmptyBottomSheet(val album: Album): AlbumScreenState
    data class BottomSheet(val album: Album): AlbumScreenState
    object EmptyShare: AlbumScreenState
    data class Share(val album: Album): AlbumScreenState
    data class Dots(val album: Album): AlbumScreenState
}