package com.practicum.playlistmaker.player.ui

import com.practicum.playlistmaker.search.domain.model.Track

interface PlayerView {
    fun drawScreen(track: Track)
    fun startAnimationAlfa()
    fun startAnimationScale()
    fun showToast()
    fun changeImageForPlayButton(image: Int)
    fun updateTime(time: Int)
}