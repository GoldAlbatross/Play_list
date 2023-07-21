package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.features.itunes_api.domain.api.SearchInteractor
import com.practicum.playlistmaker.features.itunes_api.domain.iml.SearchInteractorImpl
import com.practicum.playlistmaker.features.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.features.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.features.storage.db_favorite.domain.api.FavoriteInteractor
import com.practicum.playlistmaker.features.storage.db_favorite.domain.impl.FavoriteInteractorImpl
import com.practicum.playlistmaker.features.storage.preferences.domain.api.StorageInteractor
import com.practicum.playlistmaker.features.storage.preferences.domain.impl.StorageInteractorImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val interactorModule = module {

    singleOf(::SearchInteractorImpl).bind<SearchInteractor>()
    singleOf(::StorageInteractorImpl).bind<StorageInteractor>()
    singleOf(::PlayerInteractorImpl).bind<PlayerInteractor>()
    singleOf(::FavoriteInteractorImpl).bind<FavoriteInteractor>()
}