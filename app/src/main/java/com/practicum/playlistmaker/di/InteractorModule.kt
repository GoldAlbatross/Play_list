package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.features.itunes_api.domain.api.SearchInteractor
import com.practicum.playlistmaker.features.itunes_api.domain.iml.SearchInteractorImpl
import com.practicum.playlistmaker.features.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.features.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.features.shared_preferences.domain.api.LocalStorageInteractor
import com.practicum.playlistmaker.features.shared_preferences.domain.impl.LocalStorageInteractorImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val interactorModule = module {
    singleOf(::SearchInteractorImpl).bind<SearchInteractor>()
    singleOf(::LocalStorageInteractorImpl).bind<LocalStorageInteractor>()
    singleOf(::PlayerInteractorImpl).bind<PlayerInteractor>()
}