package com.practicum.playlistmaker

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharing = findViewById<Button>(R.id.btn_sharing)
        sharing.setOnClickListener {
            val sendIntent: Intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        val support = findViewById<Button>(R.id.btn_support)
        support.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                putExtra(Intent.EXTRA_EMAIL, arrayOf("goldalbatross@yandex.ru"))
                putExtra(Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
                putExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!")
                data = Uri.parse("mailto:")
            }
            try { startActivity(intent) }
            catch (e : ActivityNotFoundException) {
                Toast.makeText(this, "Нет подходящего приложения", Toast.LENGTH_SHORT).show()
            }
        }

        val agreement = findViewById<Button>(R.id.btn_agreement)
        agreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            }
            try { startActivity(intent) }
            catch (e : ActivityNotFoundException) {
                Toast.makeText(this, "Нет подходящего приложения", Toast.LENGTH_SHORT).show()
            }
        }
    }
}