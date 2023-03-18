package com.practicum.playlistmaker

import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.model.Track
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MediaLibActivity : AppCompatActivity(R.layout.activity_player) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val img = findViewById<ImageView>(R.id.image)
        val title = findViewById<TextView>(R.id.txt_track)
        val artist = findViewById<TextView>(R.id.txt_artist)
        val duration = findViewById<TextView>(R.id.txt_duration_right)
        val album = findViewById<TextView>(R.id.txt_album_right)
        val year = findViewById<TextView>(R.id.txt_year_right)
        val genre = findViewById<TextView>(R.id.txt_style_right)
        val country = findViewById<TextView>(R.id.txt_country_right)

        val track = when {
            SDK_INT >= 33 -> intent.getParcelableExtra(SearchActivity.KEY_TRACK, Track::class.java)
            else -> @Suppress("DEPRECATION") intent.getParcelableExtra(SearchActivity.KEY_TRACK) as? Track
        }

        if (track != null) {
            Glide.with(this)
                .load(track.url.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.player_placeholder)
                .transform(RoundedCorners(8))
                .into(img)
            title.text = track.track
            artist.text = track.artist
            duration.text = SimpleDateFormat("mm:ss", Locale.US).format(track.trackTime)
            album.text = track.artist
            year.text = track.releaseDate.substring(0, 4)
            album.text = if (track.collectionName.isNullOrEmpty()) "" else track.collectionName
            genre.text = track.primaryGenreName
            country.text = track.country
        }

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }

    }

}