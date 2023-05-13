package com.practicum.playlistmaker.settings.ui.router

import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import com.practicum.playlistmaker.R

class SettingsRouter(private val context: Context) {

    fun sharingPlayListMaker() {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, HTTP_PRACTICUM)
            type = "text/plain"
            createChooser(this,null)
            context.startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    fun getSupport() {
        Intent(Intent.ACTION_SENDTO).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(SUPPORT_EMAIL))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.theme_email))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_message))
            data = Uri.parse("mailto:")
            createChooser(this, null)
            context.startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    fun readAgreement() {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(HTTP_OFFER)
            createChooser(this,null)
            context.startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    private companion object {
        const val HTTP_PRACTICUM = "https://practicum.yandex.ru/android-developer/"
        const val HTTP_OFFER = "https://yandex.ru/legal/practicum_offer/"
        const val SUPPORT_EMAIL = "goldalbatross@yandex.ru"
    }
}