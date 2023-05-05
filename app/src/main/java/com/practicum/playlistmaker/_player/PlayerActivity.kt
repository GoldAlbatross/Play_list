package com.practicum.playlistmaker._player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.MediaPlayerRepository
import com.practicum.playlistmaker.data.Track
import com.practicum.playlistmaker.domain.player_interactor.PlayerInteractor
import com.practicum.playlistmaker.presenters.player.PlayerPresenter
import com.practicum.playlistmaker.presenters.player.PlayerRouter
import com.practicum.playlistmaker.presenters.player.IPlayerView
import com.practicum.playlistmaker.tools.DELAY_2000
import com.practicum.playlistmaker.tools.DELAY_3000
import com.practicum.playlistmaker.tools.Debouncer
import com.practicum.playlistmaker.tools.debounceClickListener
import com.practicum.playlistmaker.tools.getTimeFormat

class PlayerActivity : AppCompatActivity(R.layout.activity_player), IPlayerView {

    private val debouncer = Debouncer()

    private val time: TextView by lazy { findViewById(R.id.txt_duration_right) }
    private val title: TextView by lazy { findViewById(R.id.txt_track) }
    private val artist: TextView by lazy { findViewById(R.id.txt_artist) }
    private val album: TextView by lazy { findViewById(R.id.txt_album_right) }
    private val year: TextView by lazy { findViewById(R.id.txt_year_right) }
    private val genre: TextView by lazy { findViewById(R.id.txt_style_right) }
    private val country: TextView by lazy { findViewById(R.id.txt_country_right) }
    private val img: ImageView by lazy { findViewById(R.id.image) }
    private val btnPlay: ImageButton by lazy { findViewById(R.id.btn_play) }
    private val btnLike: ImageButton by lazy { findViewById(R.id.btn_like) }
    private val btnAdd: ImageButton by lazy { findViewById(R.id.btn_add) }
    private val currentTime: TextView by lazy { findViewById(R.id.current_time) }
    private lateinit var presenter: PlayerPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = PlayerPresenter(view = this,
            interactor = PlayerInteractor(mediaPlayer = MediaPlayerRepository()),
            router = PlayerRouter(this),
        )

        val btnBack = findViewById<Toolbar>(R.id.toolbar)
        btnBack.setNavigationOnClickListener { presenter.onClickedBack() }
        btnLike.debounceClickListener(debouncer) { flipAnimation(btnLike) }
        btnAdd.debounceClickListener(debouncer) { flipAnimation(btnAdd) }
        btnPlay.apply {
            alpha = 0.1f
            btnPlay.animate().apply { duration = DELAY_3000; rotationYBy(360f) }
            debounceClickListener(debouncer) { presenter.onClickedPlay() }
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.pauseMusic()
    }
    override fun onDestroy() {
        super.onDestroy()
        presenter.stopMediaPlayer()
    }
    private fun flipAnimation(view: ImageButton) {
        view.animate().apply { duration = DELAY_2000; rotationYBy(1080f) }
    }
    override fun drawScreen(track: Track) {
        Glide.with(this)
            .load(track.url.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.player_placeholder)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.size_8dp)))
            .into(img)
        title.text = track.track
        artist.text = track.artist
        time.text = track.trackTime.getTimeFormat()
        album.text = track.artist
        year.text = track.releaseDate.substring(0, 4)
        album.text = track.collectionName.ifEmpty { "" }
        genre.text = track.primaryGenreName
        country.text = track.country
    }
    override fun startAnimationAlfa() {
        btnPlay.animate().apply { duration = DELAY_2000; alpha(1.0f) }
    }
    override fun startAnimationScale() {
        btnPlay.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale))
    }
    override fun showToast() {
        Toast.makeText(this, getString(R.string.loading),Toast.LENGTH_LONG).show()
    }
    override fun changeImageForPlayButton(image: Int) {
        btnPlay.setImageResource(image)
    }
    override fun updateTime(time: Int) {
        currentTime.text = time.getTimeFormat()
    }
}
