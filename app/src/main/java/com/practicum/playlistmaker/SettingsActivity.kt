package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val switch = findViewById<SwitchCompat>(R.id.switch1)
        switch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){ AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
            else{ AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
        }

        val sharing = findViewById<Button>(R.id.btn_sharing)
        sharing.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/")
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
        }

        val support = findViewById<Button>(R.id.btn_support)
        support.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                putExtra(Intent.EXTRA_EMAIL, arrayOf("goldalbatross@yandex.ru"))
                putExtra(Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
                putExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!")
                data = Uri.parse("mailto:")
                startActivity(Intent.createChooser(this, null))
            }
        }

        val agreement = findViewById<Button>(R.id.btn_agreement)
        agreement.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://yandex.ru/legal/practicum_offer/")
                startActivity(Intent.createChooser(this, null))
            }
        }
    }
}