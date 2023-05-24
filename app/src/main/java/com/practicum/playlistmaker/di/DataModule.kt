package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.practicum.playlistmaker.app.App
import com.practicum.playlistmaker.features.itunes_api.data.SearchRepositoryImpl
import com.practicum.playlistmaker.features.itunes_api.data.network.ApiITunes
import com.practicum.playlistmaker.features.itunes_api.data.network.NetworkClient
import com.practicum.playlistmaker.features.itunes_api.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.features.itunes_api.domain.api.SearchRepository
import com.practicum.playlistmaker.features.player.data.PlayerImpl
import com.practicum.playlistmaker.features.player.domain.api.Player
import com.practicum.playlistmaker.features.shared_preferences.data.LocalStorageImpl
import com.practicum.playlistmaker.features.shared_preferences.data.converter.DataConverter
import com.practicum.playlistmaker.features.shared_preferences.data.converter.GsonDataConverter
import com.practicum.playlistmaker.features.shared_preferences.domain.api.LocalStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single { Gson() }
    single { MediaPlayer() }
    single { HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) }
    single {
        val logging = get<HttpLoggingInterceptor>()
        OkHttpClient.Builder().addInterceptor(logging).build()
    }
    single<ApiITunes> {
        //val client = get<OkHttpClient>()
        Retrofit.Builder()
            .baseUrl(RetrofitNetworkClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            //.client(client)
            .build()
            .create(ApiITunes::class.java)
    }
    single {
        androidContext().getSharedPreferences(App.PREF_KEY, AppCompatActivity.MODE_PRIVATE)
    }

    singleOf(::LocalStorageImpl).bind<LocalStorage>()
    singleOf(::GsonDataConverter).bind<DataConverter>()
    singleOf(::PlayerImpl).bind<Player>()
    singleOf(::SearchRepositoryImpl).bind<SearchRepository>()
    singleOf(::RetrofitNetworkClient).bind<NetworkClient>()

}