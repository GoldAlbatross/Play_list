package com.practicum.playlistmaker.features.shared_preferences.domain.impl


import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.features.shared_preferences.domain.api.LocalStorage
import com.practicum.playlistmaker.features.shared_preferences.domain.api.LocalStorageInteractor
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class LocalStorageInteractorImpl(
    private val localStorage: LocalStorage
): LocalStorageInteractor {

    private val lock = ReentrantReadWriteLock()

    override fun saveSwitcherState(key: String, themeState: Boolean) {
        localStorage.writeData(key = key, data = themeState)
    }

    override fun getSwitcherState(key: String): Boolean {
        return localStorage.readData(key = key, defaultValue = false)
    }

    override fun removeTrackFromLocalStorage(key: String, track: Track) {
        lock.write {
            val tracks = localStorage
                .readData(key = key, defaultValue = arrayOf<Track>())
                .toMutableList()
            tracks.remove(track)
            localStorage.writeData(key = key, data = tracks.toTypedArray())
        }
    }

    override fun saveTrackAsFirstToLocalStorage(key: String, track: Track) {
        lock.write {
            val tracks = localStorage
                .readData(key = key, defaultValue = arrayOf<Track>())
                .toMutableList()
            tracks.remove(track)
            tracks.add(0, track)
            if (tracks.size > HISTORY_MAX_SIZE) tracks.removeAt(9)
            localStorage.writeData(key = key, data = tracks.toTypedArray())
        }
    }

    override fun getTracksFromLocalStorage(key: String): MutableList<Track> {
        lock.read {
            return localStorage
                .readData(key = key, defaultValue = arrayOf<Track>())
                .toMutableList()
        }
    }

    override fun clearTrackList(key: String) {
        lock.read {
            localStorage.writeData(key, data = arrayOf<Track>())
        }
    }

    private companion object { const val HISTORY_MAX_SIZE = 10 }
}