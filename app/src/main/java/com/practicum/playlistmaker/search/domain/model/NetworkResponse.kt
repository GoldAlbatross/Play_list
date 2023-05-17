package com.practicum.playlistmaker.search.domain.model

sealed class NetworkResponse<T>(
    val data: T? = null,
    val error: String? = null,
    val offline: String? = null,
) {
    class Success<T>(data: T): NetworkResponse<T>(data = data)
    class Error<T>(error: String): NetworkResponse<T>(error = error)
    class Offline<T>(offline: String): NetworkResponse<T>(offline = offline)
}

//sealed class NetworkResponse<T> {
//    class Success<T>(data: T): NetworkResponse<T>()
//    class Error<T>(error: String): NetworkResponse<T>()
//    class Offline<T>(offline: String): NetworkResponse<T>()
//}

//sealed class NetworkResponse {
//    class Success (val listFromApi: List<Track>): NetworkResponse()
//    object NoData : NetworkResponse()
//    class Error (val message: String): NetworkResponse()
//}
