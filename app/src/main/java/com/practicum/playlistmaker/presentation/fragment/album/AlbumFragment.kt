package com.practicum.playlistmaker.presentation.fragment.album

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.Logger
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAlbumBinding
import com.practicum.playlistmaker.domain.local_db.model.Album
import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.presentation.viewmodel.AlbumViewModel
import com.practicum.playlistmaker.utils.KEY_TRACK
import com.practicum.playlistmaker.utils.className
import com.practicum.playlistmaker.utils.getTimeFormat
import com.practicum.playlistmaker.utils.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AlbumFragment: Fragment(R.layout.fragment_album) {

    private val logger: Logger by inject()
    private val binding by viewBinding<FragmentAlbumBinding>()
    private val viewModel by viewModel<AlbumViewModel> { parametersOf(arguments?.getLong(ALBUM_KEY)) }
    private var dotsSheet: BottomSheetBehavior<ConstraintLayout>? = null
    private var trackSheet: BottomSheetBehavior<ConstraintLayout>? = null
    private val trackAdapter by lazy { AlbumAdapter() }
    private var trackDialog: AlertDialog? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logger.log("$className -> onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        dotsSheet = BottomSheetBehavior.from(binding.bottomSheetDots.root)
        trackSheet = BottomSheetBehavior.from(binding.albumBottomSheet.root)

        initListeners()
        viewModel.getAlbum()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                logger.log("$className -> uiState.collect { state = ${state.className}")
                when (state) {
                    is AlbumScreenState.Default -> {  }
                    is AlbumScreenState.BottomSheet -> { drawScreenWithBottomSheet(state.album) }
                    is AlbumScreenState.EmptyShare -> { showSnack(false) }
                    is AlbumScreenState.Share -> { showApps(state.album) }
                    is AlbumScreenState.Dots -> { showBottomSheetDots(state.album) }
                    is AlbumScreenState.EmptyBottomSheet -> {
                        showSnack(true)
                        drawScreen(state.album)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        logger.log("$className -> onDestroyView()")
        super.onDestroyView()
        trackAdapter.action = null
        trackAdapter.longPress = null
        trackSheet = null
        dotsSheet = null
    }

    private fun showApps(album: Album) {
        logger.log("$className -> showApps(album: Album)")
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, convertAlbumToString(album))
            type = "text/plain"
        }
        val chooserIntent = Intent.createChooser(shareIntent, "Share APK")
        activity?.startActivity(chooserIntent)
    }

    private fun convertAlbumToString(album: Album): String {
        logger.log("$className -> convertAlbumToString(album: Album): String")
        val sb = StringBuilder()
        sb.append("${album.name}\n")
        sb.append("${album.description}\n")
        sb.append(resources.getQuantityString(R.plurals.count, album.trackCount, album.trackCount) + "\n\n")

        for ((index, track) in album.trackList.withIndex()) {
            sb.append("${index + 1}. ${track.artist} — ${track.track} (${track.trackTime.getTimeFormat()})\n")
        }
        return sb.toString()
    }

    private fun showSnack(emptyBottomSheet: Boolean) {
        logger.log("$className -> showSnack(emptyBottomSheet: $emptyBottomSheet)")
        val message =
            if (emptyBottomSheet) getString(R.string.no_tracks)
            else getString(R.string.nothing_to_share)
        Snackbar
            .make(requireContext(), binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .show()
    }

    private fun initListeners() {
        logger.log("$className -> initListeners()")
        trackAdapter.action = { track ->
            findNavController().navigate(
                resId = R.id.action_albumFragment_to_playerActivity,
                args = bundleOf(KEY_TRACK to track),
            )
        }
        trackAdapter.longPress = { prepareTrackDialog(it) }
        with(binding) {
            backBtn.setNavigationOnClickListener { findNavController().popBackStack() }
            share.setOnClickListener { viewModel.onSharePressed() }
            dots.setOnClickListener { viewModel.onDotsPressed() }
        }
    }

    private fun showBottomSheetDots(album: Album) {
        logger.log("$className -> showBottomSheetDots(album: Album)")
        with(binding.bottomSheetDots) {
            item.name.text = album.name
            item.time.text = getTimeAllTracks(album.trackList)
            share.setOnClickListener { viewModel.onSharePressed() }
            remove.setOnClickListener { prepareAlbumDialog() }
            change.setOnClickListener {
                findNavController().navigate(
                    resId = R.id.action_albumFragment_to_editFragment,
                    args = bundleOf(KEY_TRACK to album),
                )
            }
            Glide.with(requireActivity()).load(album.uri)
                .placeholder(R.drawable.player_placeholder)
                .into(item.picture)
        }
        dotsSheet?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun prepareTrackDialog(trackId: Int) {
        logger.log("$className -> prepareTrackDialog(trackId: $trackId)")
        trackDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_track))
            .setNegativeButton(R.string.no) { _,_ -> trackDialog?.dismiss() }
            .setPositiveButton(R.string.yes) { _,_ -> viewModel.deleteTrack(trackId) }
            .create()
        trackDialog?.setCanceledOnTouchOutside(false)
        trackDialog?.setCancelable(false)
        trackDialog?.show()
    }

    private fun prepareAlbumDialog() {
        logger.log("$className -> prepareAlbumDialog()")
        trackDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.remove_album))
            .setNegativeButton(R.string.no) { _,_ -> trackDialog?.dismiss() }
            .setPositiveButton(R.string.yes) { _,_ -> removeAlbum() }
            .create()
        trackDialog?.setCanceledOnTouchOutside(false)
        trackDialog?.setCancelable(false)
        trackDialog?.show()
    }

    private fun removeAlbum() {
        logger.log("$className -> removeAlbum()")
        viewModel.deleteAlbum()
        findNavController().navigateUp()
    }

    private fun drawScreenWithBottomSheet(album: Album) {
        logger.log("$className -> drawScreenWithBottomSheet(album: Album)")
        drawScreen(album)
        trackAdapter.trackList.apply { clear(); addAll(album.trackList) }
        binding.albumBottomSheet.recycler.adapter = trackAdapter
        trackAdapter.notifyDataSetChanged()
        trackSheet?.isHideable = false
        trackSheet?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun drawScreen(album: Album) {
        logger.log("$className -> drawScreen(album: Album)")
        dotsSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        Glide.with(requireActivity())
            .load(album.uri)
            .centerCrop()
            .placeholder(R.drawable.player_placeholder)
            .into(binding.image)
        with(binding) {
            name.text = album.name
            year.text = album.description
            time.text = getTimeAllTracks(album.trackList)
            trackCount.text = getTrackCount(album.trackCount)
        }
        if (album.trackList.isEmpty()) {
            trackSheet?.isHideable = true
            trackSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun getTimeAllTracks(list: List<Track>): String {
        val totalMilliseconds = list.fold(0L) { acc, track -> acc + track.trackTime }
        val totalMinutes = (totalMilliseconds / (1000 * 60)).toInt()
        return resources.getQuantityString(R.plurals.minutes, totalMinutes, totalMinutes)
    }

    private fun getTrackCount(count: Int): String {
        return resources.getQuantityString(R.plurals.count, count, count)
    }

    companion object { const val ALBUM_KEY = "album_key" }
}