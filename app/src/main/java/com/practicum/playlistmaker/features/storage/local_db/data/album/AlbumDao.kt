package com.practicum.playlistmaker.features.storage.local_db.data.album

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createAlbum(album: AlbumEntity)

//    @Delete
//    suspend fun deletePlaylist(album: AlbumEntity)
//
//    @Update
//    suspend fun updatePlaylist(album: AlbumEntity)

    @Query("SELECT * FROM album ORDER BY date DESC;")
    fun getAlbums(): Flow<List<AlbumEntity>>
}
