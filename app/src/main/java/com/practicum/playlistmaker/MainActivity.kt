package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSearch = findViewById<Button>(R.id.btn_search)
        val btnClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@MainActivity, Search::class.java))
            }
        }
        btnSearch.setOnClickListener(btnClickListener)

        val btnMediaLib = findViewById<Button>(R.id.btn_media)
        btnMediaLib.setOnClickListener {
            startActivity(Intent(this@MainActivity, MediaLib::class.java))
        }

        val btnSettings = findViewById<Button>(R.id.btn_settings)
        btnSettings.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }
    }
}