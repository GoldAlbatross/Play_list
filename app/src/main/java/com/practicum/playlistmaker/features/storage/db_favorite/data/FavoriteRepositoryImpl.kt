package com.practicum.playlistmaker.features.storage.db_favorite.data

import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.features.storage.db_favorite.domain.api.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val localDatabase: LocalDatabase,
    private val trackDbConvertor: TrackDbConvertor,
): FavoriteRepository {

    override suspend fun addToFavorite(track: Track) {
        localDatabase.getDao().addToFavorite(trackDbConvertor.map(track))
    }

    override suspend fun removeFromFavorite(id: Int) {
        localDatabase.getDao().removeFromFavorite(id)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        emit(localDatabase.getDao().getFavoriteTracks().map { trackDbConvertor.map(it) })
    }

    override fun isFavorite(id: Int): Flow<Boolean> = flow {
        emit(localDatabase.getDao().isFavorite(id))
    }

}