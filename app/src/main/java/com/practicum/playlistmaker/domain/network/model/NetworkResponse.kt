package com.practicum.playlistmaker.domain.network.model

sealed interface NetworkResponse<T> {

    data class Success<T>(val data: T): NetworkResponse<T>
    class NoData<T>(val message: String): NetworkResponse<T>
    class Error<T>(val message: String): NetworkResponse<T>
    class Offline<T>(val message: String): NetworkResponse<T>
}