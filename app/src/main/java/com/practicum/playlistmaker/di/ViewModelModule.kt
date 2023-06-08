package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.screens.root.view_model.RootViewModel
import com.practicum.playlistmaker.screens.media_lib.view_model.FavoriteListViewModel
import com.practicum.playlistmaker.screens.media_lib.view_model.PlayListViewModel
import com.practicum.playlistmaker.screens.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.screens.search.view_model.SearchViewModel
import com.practicum.playlistmaker.screens.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.bind

val viewModelModule = module {
    viewModelOf(::RootViewModel).bind()
    viewModelOf(::PlayerViewModel).bind()
    viewModelOf(::SearchViewModel).bind()
    viewModelOf(::SettingsViewModel).bind()
    viewModelOf(::PlayListViewModel).bind()
    viewModelOf(::FavoriteListViewModel).bind()
}