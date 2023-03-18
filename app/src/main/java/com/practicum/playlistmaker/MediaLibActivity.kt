package com.practicum.playlistmaker

import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.model.Track

class MediaLibActivity : AppCompatActivity(R.layout.activity_player) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val img = findViewById<ImageView>(R.id.image)
        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }

        val track = when {
            SDK_INT >= 33 -> intent.getSerializableExtra(SearchActivity.KEY_TRACK, Track::class.java)
            else -> @Suppress("DEPRECATION") intent.getSerializableExtra(SearchActivity.KEY_TRACK) as? Track
        }

        if (track != null) {
            Glide.with(this)
                .load(track.url.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.player_placeholder)
                .transform(RoundedCorners(9))
                .into(img)
        }

    }
}