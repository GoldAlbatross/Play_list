package com.practicum.playlistmaker.data.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.local_db.playlist.AlbumDao
import com.practicum.playlistmaker.data.local_db.playlist.AlbumEntity
import com.practicum.playlistmaker.data.local_db.favorite.FavoriteTrackDao
import com.practicum.playlistmaker.data.local_db.favorite.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class, AlbumEntity::class]
)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun getFavoriteDao(): FavoriteTrackDao
    abstract fun getAlbumDao(): AlbumDao
}