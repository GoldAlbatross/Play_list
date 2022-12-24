package com.practicum.playlistmaker
/*
git init
git add .
git commit -m "message"
git remote add origin link
git push -u origin master
git checkout -b dev
 */

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSearch = findViewById<Button>(R.id.btn_search)
        val btnClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) { Toast
                .makeText(this@MainActivity, "Поиск!", Toast.LENGTH_LONG)
                .show() }
        }
        btnSearch.setOnClickListener(btnClickListener)

        val btnMediaLib = findViewById<Button>(R.id.btn_media)
        btnMediaLib.setOnClickListener { Toast
            .makeText(this@MainActivity, "Медиатека!", Toast.LENGTH_LONG)
            .show() }

        val btnSettings = findViewById<Button>(R.id.btn_settings)
        btnSettings.setOnClickListener { Toast
            .makeText(this@MainActivity, "Настройки!", Toast.LENGTH_LONG)
            .show() }
    }
}