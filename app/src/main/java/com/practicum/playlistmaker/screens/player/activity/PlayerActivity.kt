package com.practicum.playlistmaker.screens.player.activity

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.screens.player.router.PlayerRouter
import com.practicum.playlistmaker.screens.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.screens.player.model.AddButtonModel
import com.practicum.playlistmaker.screens.player.model.LikeButtonModel
import com.practicum.playlistmaker.screens.player.model.PlayButtonState
import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.utils.DELAY_1500
import com.practicum.playlistmaker.utils.DELAY_2000
import com.practicum.playlistmaker.utils.DELAY_3000
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener
import com.practicum.playlistmaker.utils.getTimeFormat

class PlayerActivity : AppCompatActivity() {

    private val debouncer = Debouncer()
    private val router by lazy { PlayerRouter(this) }
    private val binding by lazy { ActivityPlayerBinding.inflate(layoutInflater) }
    private val viewModel: PlayerViewModel by viewModel()
    private val currentTime: TextView by lazy { findViewById(R.id.current_time) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val track = router.getTrackFromIntent()
        drawScreen(track = track)

        binding.backBtn.setNavigationOnClickListener { router.onClickedBack() }
        binding.btnLike.debounceClickListener(debouncer) { viewModel.onClickLike() }
        binding.btnAdd.debounceClickListener(debouncer) { viewModel.onClickAdd() }
        binding.btnPlay.apply {
            alpha = 0.1f
            binding.btnPlay.animate().apply { duration = DELAY_3000; rotationYBy(360f) }
            debounceClickListener(debouncer) { viewModel.onClickedPlay() }
        }

        viewModel.addButtonStateLiveData().observe(this) { state ->
            when (state) {
                AddButtonModel.Add -> flipAnimation(binding.btnAdd)
                AddButtonModel.Remove -> { /*TODO*/ }
            }
        }

        viewModel.playButtonStateLiveData().observe(this) { state ->
            when (state) {
                is PlayButtonState.Prepare -> {
                    if (state.clicked) { startAnimationScale(); showToast() }
                }
                is PlayButtonState.PrepareDone -> {
                    startAnimationAlfa()
                    changeImageForPlayButton(R.drawable.player_play)
                }
                is PlayButtonState.Play -> {
                    startAnimationScale()
                    changeImageForPlayButton(R.drawable.player_pause)
                }
                is PlayButtonState.Pause -> {
                    startAnimationScale()
                    changeImageForPlayButton(R.drawable.player_play)
                }
            }
        }

        viewModel.likeButtonStateLiveData().observe(this) { state ->
            when (state) {
                LikeButtonModel.DisLike -> { /*TODO*/ }
                LikeButtonModel.Like -> flipAnimation(binding.btnLike)
            }
        }

        viewModel.timeStateLiveData().observe(this) { updateTime(it)}




    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseMusic()
    }

    private fun flipAnimation(view: ImageButton) {
        view.animate().apply { duration = DELAY_2000; rotationYBy(1080f) }
    }
    private fun drawScreen(track: Track) {
        viewModel.preparePlayer(trackLink = track.previewUrl)
        Glide.with(this)
            .load(track.url.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.player_placeholder)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.size_8dp)))
            .into(binding.image)
        binding.txtTrack.text = track.track
        binding.txtArtist.text = track.artist
        binding.txtDurationRight.text = track.trackTime.getTimeFormat()
        binding.txtAlbumRight.text = track.artist
        binding.txtYearRight.text = track.releaseDate.substring(0, 4)
        binding.txtAlbumRight.text = track.collectionName.ifEmpty { "" }
        binding.txtStyleRight.text = track.primaryGenreName
        binding.txtCountryRight.text = track.country
    }
    private fun startAnimationAlfa() {
        binding.btnPlay.animate().apply { duration = DELAY_1500; alpha(1.0f) }
    }
    private fun startAnimationScale() {
        binding.btnPlay.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale))
    }
    private fun showToast() {
        Toast.makeText(this, getString(R.string.loading),Toast.LENGTH_LONG).show()
    }
    private fun changeImageForPlayButton(image: Int) {
        binding.btnPlay.setImageResource(image)
    }
    private fun updateTime(time: String) {
        currentTime.text = time
    }
}
