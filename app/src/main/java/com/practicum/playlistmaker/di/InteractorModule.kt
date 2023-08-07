package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.network.api.SearchInteractor
import com.practicum.playlistmaker.domain.network.iml.SearchInteractorImpl
import com.practicum.playlistmaker.domain.player.api.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.local_db.api.PlayListInteractor
import com.practicum.playlistmaker.domain.local_db.api.FavoriteInteractor
import com.practicum.playlistmaker.domain.local_db.impl.PlayListInteractorImpl
import com.practicum.playlistmaker.domain.local_db.impl.FavoriteInteractorImpl
import com.practicum.playlistmaker.domain.shared_prefs.api.StorageInteractor
import com.practicum.playlistmaker.domain.shared_prefs.impl.StorageInteractorImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val interactorModule = module {

    singleOf(::SearchInteractorImpl).bind<SearchInteractor>()
    singleOf(::StorageInteractorImpl).bind<StorageInteractor>()
    singleOf(::PlayerInteractorImpl).bind<PlayerInteractor>()
    singleOf(::FavoriteInteractorImpl).bind<FavoriteInteractor>()
    singleOf(::PlayListInteractorImpl).bind<PlayListInteractor>()
}