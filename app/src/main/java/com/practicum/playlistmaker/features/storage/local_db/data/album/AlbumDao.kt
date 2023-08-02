package com.practicum.playlistmaker.features.storage.local_db.data.album

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createAlbum(album: AlbumEntity)

    @Query("UPDATE album SET trackList = :trackList, trackCount = :trackCount, date = :date WHERE id = :id")
    suspend fun updateAlbumFields(id: Long, trackList: String, trackCount: Int, date: Long,)

    @Query("SELECT * FROM album ORDER BY date DESC;")
    fun getAlbums(): Flow<List<AlbumEntity>>
}
