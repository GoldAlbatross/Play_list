package com.practicum.playlistmaker.features.storage.db_favorite.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(track: TrackEntity)

    @Query("DELETE FROM favorite_tracks WHERE id = :id")
    suspend fun removeFromFavorite(id: Int)

    @Query("SELECT * FROM favorite_tracks ORDER BY joinDate DESC;")
    suspend fun getFavoriteTracks(): List<TrackEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_tracks WHERE id = :id LIMIT 1);")
    suspend fun isFavorite(id: Int): Boolean
}