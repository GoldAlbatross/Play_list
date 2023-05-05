package com.practicum.playlistmaker.data.raw

interface BooleanStorage {

    fun addBoolean (state: Boolean)

    fun getBoolean (): Boolean
}