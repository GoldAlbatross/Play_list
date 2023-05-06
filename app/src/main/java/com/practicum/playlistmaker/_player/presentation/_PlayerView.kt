package com.practicum.playlistmaker._player.presentation

import com.practicum.playlistmaker._player.domain.model.Track

interface _PlayerView {
    fun drawScreen(track: Track)
    fun startAnimationAlfa()
    fun startAnimationScale()
    fun showToast()
    fun changeImageForPlayButton(image: Int)
    fun updateTime(time: Int)



}