package com.practicum.playlistmaker.presentation.fragment.edit

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.app.TAG
import com.practicum.playlistmaker.domain.local_db.model.Album
import com.practicum.playlistmaker.presentation.fragment.create.CreateFragment
import com.practicum.playlistmaker.presentation.viewmodel.EditViewModel
import com.practicum.playlistmaker.utils.className
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditFragment : CreateFragment() {

    override val viewModel by viewModel<EditViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "${className()} -> onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        drawScreen(album!!)
    }

    private fun drawScreen(album: Album) {
        Log.d(TAG, "${className()} -> onViewCreated()")
        binding.apply {
            name.setText(album.name)
            description.setText(album.description)
            create.text = activity?.getString(R.string.save)
            toolbar.title = getString(R.string.edit)
        }
        Glide.with(this)
            .load(album.uri)
            .transform(RoundedCorners(8))
            .into(binding.frameImage)
    }
}