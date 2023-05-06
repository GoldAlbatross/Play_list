package com.practicum.playlistmaker.data.not_processed

interface BooleanStorage {

    fun addBoolean (state: Boolean)

    fun getBoolean (): Boolean
}