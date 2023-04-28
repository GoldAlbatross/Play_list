package com.practicum.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.tools.DELAY_2000
import com.practicum.playlistmaker.tools.DELAY_300
import com.practicum.playlistmaker.tools.Debouncer
import com.practicum.playlistmaker.tools.debounceClickListener
import com.practicum.playlistmaker.tools.getParcelableFromIntent
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity(R.layout.activity_player) {

    private val debouncer = Debouncer()
    private var mediaPlayer = MediaPlayer()
    private var handler = Handler(Looper.getMainLooper())
    private var state = PlayerStates.DEFAULT
    private lateinit var currentTime: TextView
    private lateinit var btnPlay: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val time = findViewById<TextView>(R.id.txt_duration_right)
        currentTime = findViewById(R.id.current_time)
        val img = findViewById<ImageView>(R.id.image)
        val title = findViewById<TextView>(R.id.txt_track)
        val artist = findViewById<TextView>(R.id.txt_artist)
        val album = findViewById<TextView>(R.id.txt_album_right)
        val year = findViewById<TextView>(R.id.txt_year_right)
        val genre = findViewById<TextView>(R.id.txt_style_right)
        val country = findViewById<TextView>(R.id.txt_country_right)
        btnPlay = findViewById(R.id.btn_play)
        val btnLike = findViewById<ImageButton>(R.id.btn_like)
        val btnAdd = findViewById<ImageButton>(R.id.btn_add)
        val btnBack = findViewById<Toolbar>(R.id.toolbar)

        val track = intent.getParcelableFromIntent(SearchActivity.KEY_TRACK, Track::class.java)
        val animScale = AnimationUtils.loadAnimation(this, R.anim.scale)

        Glide.with(this)
            .load(track.url.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.player_placeholder)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.size_8dp)))
            .into(img)
        title.text = track.track
        artist.text = track.artist
        time.text = SimpleDateFormat("mm:ss", Locale.US).format(track.trackTime)
        album.text = track.artist
        year.text = track.releaseDate.substring(0, 4)
        album.text = track.collectionName.ifEmpty { "" }
        genre.text = track.primaryGenreName
        country.text = track.country

        btnLike.debounceClickListener(debouncer) { flipAnimation(btnLike) }
        btnAdd.debounceClickListener(debouncer) { flipAnimation(btnAdd) }
        btnPlay.apply {
            alpha = 0.1f
            btnPlay.animate().apply { duration = 3000; rotationYBy(360f) }
            debounceClickListener(debouncer) {
                clickHandler(this)
                btnPlay.startAnimation(animScale)
            }
        }
        btnBack.setNavigationOnClickListener { finish() }

        mediaPlayer.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                btnPlay.animate().apply { duration = 1500; alpha(1.0f) }
                state = PlayerStates.PREPARED
            }
            setOnCompletionListener {
                btnPlay.setImageResource(R.drawable.player_play)

                handler.removeCallbacksAndMessages(null)
                state = PlayerStates.PREPARED
            }
        }

    }

    override fun onPause() {
        super.onPause()
        pauseMusic(btnPlay)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mediaPlayer.release()
    }
    private fun clickHandler(view: ImageButton) {
        when (state) {
            PlayerStates.DEFAULT -> Toast.makeText(this, "Loading track...",Toast.LENGTH_LONG).show()
            PlayerStates.PREPARED -> playMusic(view)
            PlayerStates.PLAYING -> pauseMusic(view)
            PlayerStates.PAUSED -> playMusic(view)
        }
    }
    private fun playMusic(view: ImageButton) {
        mediaPlayer.start()
        state = PlayerStates.PLAYING
        view.setImageResource(R.drawable.player_pause)
        handler.post(object : Runnable {
            override fun run() {
                currentTime.text = SimpleDateFormat("mm:ss",Locale.US).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, DELAY_300)
            }
        })
    }
    private fun pauseMusic(view: ImageButton) {
        mediaPlayer.pause()
        state = PlayerStates.PAUSED
        view.setImageResource(R.drawable.player_play)
        handler.removeCallbacksAndMessages(null)
    }

    private fun flipAnimation(view: ImageButton) {
        view.animate().apply { duration = DELAY_2000; rotationYBy(1080f) }
    }
}
