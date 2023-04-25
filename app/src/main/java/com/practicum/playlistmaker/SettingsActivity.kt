package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.practicum.playlistmaker.tools.Debouncer
import com.practicum.playlistmaker.tools.debounceClickListener

class SettingsActivity : AppCompatActivity() {

    private val debouncer = Debouncer()
    private lateinit var swtch: SwitchCompat
    private lateinit var sharing: Button
    private lateinit var support: Button
    private lateinit var agreement: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        swtch = findViewById(R.id.night_swtch)
        sharing = findViewById(R.id.btn_sharing)
        support = findViewById(R.id.btn_support)
        agreement = findViewById(R.id.btn_agreement)
        swtch.isChecked = App.instance.themeSwitcher.getBoolean()

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }

    }

    override fun onResume() {
        super.onResume()

        swtch.setOnCheckedChangeListener { _, isChecked ->
            App.instance.themeSwitcher.addBoolean(isChecked)
        }

        sharing.debounceClickListener(debouncer) {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, http_practicum)
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
        }

        support.debounceClickListener(debouncer) {
            Intent(Intent.ACTION_SENDTO).apply {
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email_albatross))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.theme_email))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
                data = Uri.parse("mailto:")
                startActivity(Intent.createChooser(this, null))
            }
        }

        agreement.debounceClickListener(debouncer) {
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