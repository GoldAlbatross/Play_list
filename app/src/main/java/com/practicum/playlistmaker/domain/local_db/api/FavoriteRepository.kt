package com.practicum.playlistmaker.domain.local_db.api

import com.practicum.playlistmaker.domain.network.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun addToFavorite(track: Track)
    suspend fun removeFromFavorite(id: Int)
    fun getFavoriteTracks(): Flow<List<Track>>
    fun isFavorite(id: Int): Flow<Boolean>
}