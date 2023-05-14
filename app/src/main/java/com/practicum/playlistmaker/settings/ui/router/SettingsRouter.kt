package com.practicum.playlistmaker.settings.ui.router

import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R

class SettingsRouter(private val activity: AppCompatActivity) {

    fun onClickedSharing() {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, HTTP_PRACTICUM)
            type = "text/plain"
            createChooser(this,null)
            activity.startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    fun onClickedSupport() {
        Intent(Intent.ACTION_SENDTO).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(SUPPORT_EMAIL))
            putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.theme_email))
            putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.email_message))
            data = Uri.parse("mailto:")
            createChooser(this, null)
            activity.startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    fun onClickedAgreement() {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(HTTP_OFFER)
            createChooser(this,null)
            activity.startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    fun onClickedBack() { activity.finish() }

    private companion object {
        const val HTTP_PRACTICUM = "https://practicum.yandex.ru/android-developer/"
        const val HTTP_OFFER = "https://yandex.ru/legal/practicum_offer/"
        const val SUPPORT_EMAIL = "goldalbatross@yandex.ru"
    }
}