package com.practicum.playlistmaker.domain.local_db.impl

import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.domain.local_db.api.FavoriteInteractor
import com.practicum.playlistmaker.domain.local_db.api.FavoriteRepository
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