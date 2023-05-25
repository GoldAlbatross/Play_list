package com.practicum.playlistmaker.features.itunes_api.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.features.itunes_api.dto.Response
import com.practicum.playlistmaker.features.itunes_api.dto.SearchRequest

class RetrofitNetworkClient(
    private val context: Context,
    private val backendApi: ApiITunes,
): NetworkClient {


    override fun getResponseFromBackend(dto: Any): Response {
        if (!isConnected()) return Response().apply { resultCode = -1 }

        if (dto !is SearchRequest) return Response().apply { resultCode = 400 }

        val response = backendApi.getTrackList(dto.query).execute()
        return response.body()?.apply { resultCode = response.code() }
            ?: Response().apply { resultCode = response.code() }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    companion object { const val BASE_URL = "https://itunes.apple.com" }
}