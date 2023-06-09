package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.screens.root.viewModel.RootViewModel
import com.practicum.playlistmaker.screens.mediaLib.viewModel.FavoriteListViewModel
import com.practicum.playlistmaker.screens.mediaLib.viewModel.PlayListViewModel
import com.practicum.playlistmaker.screens.player.viewModel.PlayerViewModel
import com.practicum.playlistmaker.screens.search.viewModel.SearchViewModel
import com.practicum.playlistmaker.screens.settings.viewModel.SettingsViewModel
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