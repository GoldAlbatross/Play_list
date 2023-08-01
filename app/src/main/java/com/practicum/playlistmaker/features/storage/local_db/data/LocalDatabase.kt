package com.practicum.playlistmaker.features.storage.local_db.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.features.storage.local_db.data.album.AlbumDao
import com.practicum.playlistmaker.features.storage.local_db.data.album.AlbumEntity
import com.practicum.playlistmaker.features.storage.local_db.data.favorite.FavoriteTrackDao
import com.practicum.playlistmaker.features.storage.local_db.data.favorite.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class, AlbumEntity::class]
)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun getFavoriteDao(): FavoriteTrackDao
    abstract fun getAlbumDao(): AlbumDao
}