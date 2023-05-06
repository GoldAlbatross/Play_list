package com.practicum.playlistmaker.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.SettingsActivity
import com.practicum.playlistmaker.media_lib.MediaLibActivity
import com.practicum.playlistmaker.search.presentation.SearchActivity
import com.practicum.playlistmaker.tools.Debouncer
import com.practicum.playlistmaker.tools.debounceClickListener

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val debouncer = Debouncer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btnSearch = findViewById<Button>(R.id.btn_search)
        val btnMediaLib = findViewById<Button>(R.id.btn_media)
        val btnSettings = findViewById<Button>(R.id.btn_settings)

        btnSearch.debounceClickListener(debouncer) {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        btnMediaLib.debounceClickListener(debouncer) {
            startActivity(Intent(this, MediaLibActivity::class.java))
        }

        btnSettings.debounceClickListener(debouncer) {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }

}