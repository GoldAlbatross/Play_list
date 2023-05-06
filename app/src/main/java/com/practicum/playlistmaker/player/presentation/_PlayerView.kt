package com.practicum.playlistmaker.player.presentation

import com.practicum.playlistmaker.models.domain.Track

interface _PlayerView {
    fun drawScreen(track: Track)
    fun startAnimationAlfa()
    fun startAnimationScale()
    fun showToast()
    fun changeImageForPlayButton(image: Int)
    fun updateTime(time: Int)



}