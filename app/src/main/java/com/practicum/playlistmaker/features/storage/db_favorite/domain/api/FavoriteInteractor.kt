package com.practicum.playlistmaker.features.storage.db_favorite.domain.api

import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.features.storage.db_favorite.data.TrackEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {

    suspend fun addToFavorite(track: Track)
    suspend fun removeFromFavorite(id: Int)
    fun getFavoriteTracks(): Flow<List<Track>>
    fun isFavorite(id: Int): Flow<Boolean>
}