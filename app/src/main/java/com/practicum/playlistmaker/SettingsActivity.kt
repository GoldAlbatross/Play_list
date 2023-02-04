package com.practicum.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setOnClickListener { finish() }

        val swtch = findViewById<SwitchCompat>(R.id.night_swtch)
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> { swtch.isChecked = true }
            else -> { swtch.isChecked = false }
        }
        swtch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){ AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
            else{ AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
        }

        val sharing = findViewById<Button>(R.id.btn_sharing)
        sharing.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, http_practicum)
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
        }

        val support = findViewById<Button>(R.id.btn_support)
        support.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email_albatross))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.theme_email))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
                data = Uri.parse("mailto:")
                startActivity(Intent.createChooser(this, null))
            }
        }

        val agreement = findViewById<Button>(R.id.btn_agreement)
        agreement.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(http_offer)
                startActivity(Intent.createChooser(this, null))
            }
        }
    }
    companion object {
        const val http_practicum = "https://practicum.yandex.ru/android-developer/"
        const val http_offer = "https://yandex.ru/legal/practicum_offer/"
        const val email_albatross = "goldalbatross@yandex.ru"
    }
}