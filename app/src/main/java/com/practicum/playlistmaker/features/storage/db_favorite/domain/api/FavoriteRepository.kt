package com.practicum.playlistmaker.features.storage.db_favorite.domain.api

import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun addToFavorite(track: Track)
    suspend fun removeFromFavorite(id: Int)
    fun getFavoriteTracks(): Flow<List<Track>>
    fun isFavorite(id: Int): Flow<Boolean>
}