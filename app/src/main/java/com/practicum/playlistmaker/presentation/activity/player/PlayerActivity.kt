package com.practicum.playlistmaker.presentation.activity.player

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.app.TAG
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.local_db.model.Album
import com.practicum.playlistmaker.domain.local_db.model.BottomSheetUIState
import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.presentation.fragment.create.CreateFragment
import com.practicum.playlistmaker.presentation.viewmodel.PlayerViewModel
import com.practicum.playlistmaker.utils.DELAY_1500
import com.practicum.playlistmaker.utils.DELAY_3000
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener
import com.practicum.playlistmaker.utils.getTimeFormat
import com.practicum.playlistmaker.utils.className
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private val viewModel by viewModel<PlayerViewModel>()
    private val debouncer: Debouncer by lazy { Debouncer(lifecycleScope) }
    private val bottomSheetAdapter by lazy { BottomSheetAdapter(debouncer) }
    private val bottomSheetContainer by lazy { findViewById<ConstraintLayout>(R.id.bottom_sheet) }
    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(bottomSheetContainer) }
    private val router by lazy { PlayerRouter(this) }
    private val track by lazy { router.getTrackFromIntent() }
    private var viewBinding: ActivityPlayerBinding? = null
    private val binding get() = viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawScreen(track)
        initBottomSheet()
        initListeners()

        viewModel.playButtonStateLiveData().observe(this) { state ->
            Log.d(TAG, "PlayerActivity -> playButtonStateLiveData().observe(this) { state = ${state.className()} }")
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
            Log.d(TAG, "PlayerActivity -> likeButtonStateLiveData().observe(this) { state = $state }")
            if (state) binding.btnLike.setImageResource(R.drawable.player_like)
            else binding.btnLike.setImageResource(R.drawable.player_dislike)
        }

        lifecycleScope.launch {
            viewModel.addButtonStateFlow.collect { state ->
                Log.d(TAG, "PlayerActivity -> addButtonStateFlow.collect { state = ${state.className()} }")
                when (state) {
                    is BottomSheetUIState.Content -> {
                        drawBottomSheet(state.albums)
                    }
                    is BottomSheetUIState.Empty -> {
                        drawBottomSheet(emptyList())
                    }
                    is BottomSheetUIState.Success -> {
                        hideBottomSheet(getString(R.string.added_to_album, state.albumName))
                    }
                    is BottomSheetUIState.Exist -> {
                        hideBottomSheet(getString(R.string.track_exist, state.albumName))
                    }
                }
            }
        }

        viewModel.timeStateLiveData().observe(this) { updateTime(it)}
    }

    private fun drawBottomSheet(albums: List<Album>) {
        Log.d(TAG, "PlayerActivity -> drawBottomSheet(albums: $albums)")
        if (albums.isNotEmpty()) {
            binding.bottomSheet.newAlbum.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.player_containerView, CreateFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            bottomSheetAdapter.playList = albums
            binding.bottomSheet.recycler.adapter = bottomSheetAdapter
            bottomSheetAdapter.notifyDataSetChanged()
        }
    }

    private fun hideBottomSheet(message: String) {
        Log.d(TAG, "PlayerActivity -> hideBottomSheet(message: $message)")
        Snackbar
            .make(this, binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.blue))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun initBottomSheet() {
        Log.d(TAG, "PlayerActivity -> initBottomSheet()")
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) { }
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> { binding.coordinator.setBackgroundColor(
                        ContextCompat.getColor(this@PlayerActivity, R.color.transparent))
                    }
                    else -> { binding.coordinator.setBackgroundColor(
                        ContextCompat.getColor(this@PlayerActivity, R.color.background))
                    }
                }
            }
        })
    }

    private fun initListeners() {
        Log.d(TAG, "PlayerActivity -> initListeners()")
        binding.backBtn.setNavigationOnClickListener { router.onClickedBack() }
        binding.btnLike.debounceClickListener(debouncer) { viewModel.onClickLike(track) }
        binding.btnAdd.debounceClickListener(debouncer) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            viewModel.onClickAdd()
        }
        binding.btnPlay.apply {
            alpha = 0.1f
            debounceClickListener(debouncer) { viewModel.onClickedPlay() }
            animate().apply { duration = DELAY_3000; rotationYBy(360f) }
        }
        bottomSheetAdapter.action = { album ->  viewModel.onClickedAlbum(track, album) }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "PlayerActivity -> onPause()")
        viewModel.pauseMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "PlayerActivity -> onDestroy()")
        viewBinding = null
    }

    private fun drawScreen(track: Track) {
        Log.d(TAG, "PlayerActivity -> drawScreen(track: $track)")
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
        Log.d(TAG, "PlayerActivity -> startAnimationAlfa()")
        binding.btnPlay.animate().apply { duration = DELAY_1500; alpha(1.0f) }
    }
    private fun startPressAnimation() {
        Log.d(TAG, "PlayerActivity -> startPressAnimation()")
        binding.btnPlay.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale))
    }
    private fun showToast(message: String?) {
        Log.d(TAG, "PlayerActivity -> showToast(message: $message)")
        message?.let {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun changeImageForPlayButton(image: Int) {
        Log.d(TAG, "PlayerActivity -> changeImageForPlayButton(image: $image)")
        binding.btnPlay.setImageResource(image)
    }
    private fun updateTime(time: String) {
        Log.d(TAG, "PlayerActivity -> updateTime(time: $time)")
        binding.currentTime.text = time
    }
}
