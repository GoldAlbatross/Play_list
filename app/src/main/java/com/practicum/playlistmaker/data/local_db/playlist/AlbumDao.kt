package com.practicum.playlistmaker.data.local_db.playlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createAlbum(album: AlbumEntity)

    @Query("UPDATE album SET trackList = :trackList, trackCount = :trackCount, date = :date WHERE id = :id")
    suspend fun updateAlbumFields(id: Long, trackList: String, trackCount: Int, date: Long)

    @Query("SELECT * FROM album ORDER BY date DESC;")
    fun getAlbums(): Flow<List<AlbumEntity>>

    @Query("SELECT * FROM album WHERE id = :id LIMIT 1;")
    suspend fun getAlbum(id: Long): AlbumEntity


    @Query("DELETE FROM album WHERE id = :id")
    suspend fun removeAlbumById(id: Long)

}
