package com.practicum.playlistmaker.okhttp


import com.practicum.playlistmaker.model.TrackResponse
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

    internal fun getResponseFromBackend(text: String) {
        backendApi.getTrackList(text).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                listener?.onSuccess(response)
            }
            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                listener?.onError(t)
            }
        })
    }

    interface TrackRetrofitListener {
        fun onSuccess(response: Response<TrackResponse>)
        fun onError(t: Throwable)
    }
}
