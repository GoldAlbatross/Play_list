package com.practicum.playlistmaker.presenters.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.practicum.playlistmaker._player.PlayerActivity
import com.practicum.playlistmaker.data.Track
import com.practicum.playlistmaker.tools.KEY_TRACK

class SearchRouter(private val activity: AppCompatActivity) {

    fun openPlayerActivity(track: Track) {
        val intent = Intent(activity, PlayerActivity::class.java)
        intent.putExtra(KEY_TRACK, track)
        activity.startActivity(intent)
    }
}