package com.practicum.playlistmaker.storage

interface BooleanStorage {

    fun addBoolean (state: Boolean)

    fun getBoolean (): Boolean
}