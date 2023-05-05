package com.practicum.playlistmaker.presenters.player

import com.practicum.playlistmaker.data.Track

interface IPlayerView {
    fun drawScreen(track: Track)
    fun startAnimationAlfa()
    fun startAnimationScale()
    fun showToast()
    fun changeImageForPlayButton(image: Int)
    fun updateTime(time: Int)



}