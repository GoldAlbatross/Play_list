package com.practicum.playlistmaker

import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.data.network.ApiITunes
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://itunes.apple.com"
class TrackRetrofit {

    internal var listener: TrackRetrofitListener? = null
    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(logging).build()
    private val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    private val backendApi = retrofit.create(ApiITunes::class.java)

    internal fun getResponseFromBackend(query: String) {
        backendApi.getTrackList(query)
            .enqueue(object : Callback<com.practicum.playlistmaker.search.data.dto.SearchResponse> {

            override fun onResponse(call: Call<com.practicum.playlistmaker.search.data.dto.SearchResponse>, response: Response<com.practicum.playlistmaker.search.data.dto.SearchResponse>, ) {
                if (response.isSuccessful)
                    listener?.onSuccess(
                        response.body()
                            ?.trackList!!
                            .filter { it.previewUrl != null }
                            .map { mapToTrack(it) }
                    )
            }

            override fun onFailure(call: Call<com.practicum.playlistmaker.search.data.dto.SearchResponse>, t: Throwable) {
                listener?.onError(t)
            }
        })
    }
    private fun mapToTrack(trackDto: TrackDto): Track {
        return Track(
            trackDto.track,
            trackDto.artist,
            trackDto.trackTime,
            trackDto.url,
            trackDto.collectionName,
            trackDto.releaseDate,
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl!!,
        )
    }
}
interface TrackRetrofitListener {
    fun onSuccess(response: List<Track>)
    fun onError(t: Throwable)
}
