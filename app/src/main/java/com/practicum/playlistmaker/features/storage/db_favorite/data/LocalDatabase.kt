package com.practicum.playlistmaker.features.storage.db_favorite.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [TrackEntity::class])
abstract class LocalDatabase: RoomDatabase() {

    abstract fun getDao(): FavoriteTrackDao
}