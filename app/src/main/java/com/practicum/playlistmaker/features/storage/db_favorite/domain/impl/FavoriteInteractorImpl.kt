package com.practicum.playlistmaker.features.storage.db_favorite.domain.impl

import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.features.storage.db_favorite.data.TrackEntity
import com.practicum.playlistmaker.features.storage.db_favorite.domain.api.FavoriteInteractor
import com.practicum.playlistmaker.features.storage.db_favorite.domain.api.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteRepository,
): FavoriteInteractor {

    override suspend fun addToFavorite(track: Track) {
        favoriteRepository.addToFavorite(track)
    }

    override suspend fun removeFromFavorite(id: Int) {
        favoriteRepository.removeFromFavorite(id)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteRepository.getFavoriteTracks()
    }

    override fun isFavorite(id: Int): Flow<Boolean> {
        return favoriteRepository.isFavorite(id)
    }
}