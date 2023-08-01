package com.practicum.playlistmaker.screens.player.activity

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.features.storage.local_db.domain.model.Album
import com.practicum.playlistmaker.features.storage.local_db.domain.model.ScreenState
import com.practicum.playlistmaker.screens.player.model.PlayerState
import com.practicum.playlistmaker.screens.player.router.PlayerRouter
import com.practicum.playlistmaker.screens.player.viewModel.PlayerViewModel
import com.practicum.playlistmaker.utils.DELAY_1500
import com.practicum.playlistmaker.utils.DELAY_3000
import com.practicum.playlistmaker.utils.DELAY_800
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener
import com.practicum.playlistmaker.utils.getTimeFormat
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private val debouncer: Debouncer by lazy { Debouncer(lifecycleScope) }
    private val bottomSheetAdapter by lazy { BottomSheetAdapter(debouncer) }
    private val bottomSheetContainer by lazy {
        findViewById<ConstraintLayout>(R.id.bottom_sheet)
    }
    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(bottomSheetContainer)
    }
    private val router by lazy { PlayerRouter(this) }
    private val track by lazy { router.getTrackFromIntent() }
    private var viewBinding: ActivityPlayerBinding? = null
    private val binding get() = viewBinding!!
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawScreen(track)
        initListeners()
        initBottomSheet()

        viewModel.playButtonStateLiveData().observe(this) { state ->
            when (state) {
                is PlayerState.Loading -> { showToast(state.message) }
                is PlayerState.Error -> { showToast(state.message) }
                is PlayerState.Ready -> {
                    startAnimationAlfa()
                    changeImageForPlayButton(R.drawable.player_play)
                }
                is PlayerState.Play -> {
                    startPressAnimation()
                    changeImageForPlayButton(R.drawable.player_pause)
                }
                is PlayerState.Pause -> {
                    startPressAnimation()
                    changeImageForPlayButton(R.drawable.player_play)
                }
            }
        }

        viewModel.likeButtonStateLiveData().observe(this) { state ->
            if (state) binding.btnLike.setImageResource(R.drawable.player_like)
            else binding.btnLike.setImageResource(R.drawable.player_dislike)
        }

        lifecycleScope.launch {
            viewModel.addButtonStateFlow.collect { state ->
                when (state) {
                    is ScreenState.Content -> { drawBottomSheet(state.albums) }
                    is ScreenState.Empty -> { drawBottomSheet(emptyList()) }
                }
            }
        }

        viewModel.timeStateLiveData().observe(this) { updateTime(it)}
    }

    private fun initBottomSheet() {
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) { }
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> { binding.coordinator?.setBackgroundColor(
                        ContextCompat.getColor(this@PlayerActivity, R.color.transparent))
                    }
                    else -> { binding.coordinator?.setBackgroundColor(
                        ContextCompat.getColor(this@PlayerActivity, R.color.background))
                    }
                }
            }
        })
    }

    private fun drawBottomSheet(albums: List<Album>) {
        binding.bottomSheet?.recycler?.adapter = bottomSheetAdapter
        bottomSheetAdapter.playList = albums
        bottomSheetAdapter.notifyDataSetChanged()
    }

    private fun initListeners() {
        binding.backBtn.setNavigationOnClickListener { router.onClickedBack() }
        binding.btnLike.debounceClickListener(debouncer) {
            viewModel.onClickLike(track)
        }
        binding.btnAdd.debounceClickListener(debouncer) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            viewModel.onClickAdd()
        }
        binding.btnPlay.apply {
            alpha = 0.1f
            debounceClickListener(debouncer) { viewModel.onClickedPlay() }
            animate().apply { duration = DELAY_3000; rotationYBy(360f) }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }

    private fun flipAnimation(view: ImageButton) {
        view.animate().apply { duration = DELAY_800; rotationYBy(720f) }
    }
    private fun drawScreen(track: Track) {
        viewModel.getFavoriteState(track.trackId)
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
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
    private fun startAnimationAlfa() {
        binding.btnPlay.animate().apply { duration = DELAY_1500; alpha(1.0f) }
    }
    private fun startPressAnimation() {
        binding.btnPlay.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale))
    }
    private fun showToast(message: String?) {
        message?.let {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun changeImageForPlayButton(image: Int) {
        binding.btnPlay.setImageResource(image)
    }
    private fun updateTime(time: String) {
        binding.currentTime.text = time
    }
}
