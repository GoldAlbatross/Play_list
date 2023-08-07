package com.practicum.playlistmaker.data.local_db.favorite

import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.data.local_db.LocalDatabase
import com.practicum.playlistmaker.domain.local_db.api.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val localDatabase: LocalDatabase,
    private val trackDbConvertor: TrackDbConvertor,
): FavoriteRepository {

    override suspend fun addToFavorite(track: Track) {
        localDatabase.getFavoriteDao().addToFavorite(trackDbConvertor.map(track))
    }

    override suspend fun removeFromFavorite(id: Int) {
        localDatabase.getFavoriteDao().removeFromFavorite(id)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> =
        localDatabase.getFavoriteDao()
            .getFavoriteTracks()
            .map { list -> list.map { trackDbConvertor.map(it) } }


    override fun isFavorite(id: Int): Flow<Boolean> = flow {
        emit(localDatabase.getFavoriteDao().isFavorite(id))
    }

}