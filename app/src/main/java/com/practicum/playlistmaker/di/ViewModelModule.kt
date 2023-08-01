package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.screens.album_creating.CreateAlbumViewModel
import com.practicum.playlistmaker.screens.root.viewModel.RootViewModel
import com.practicum.playlistmaker.screens.media_library.childFragments.favorite_fragment.FavoriteListViewModel
import com.practicum.playlistmaker.screens.media_library.childFragments.playlist_fragment.PlayListViewModel
import com.practicum.playlistmaker.screens.player.viewModel.PlayerViewModel
import com.practicum.playlistmaker.screens.search.viewModel.SearchViewModel
import com.practicum.playlistmaker.screens.settings.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::RootViewModel)
    viewModelOf(::PlayerViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::PlayListViewModel)
    viewModelOf(::FavoriteListViewModel)
    viewModelOf(::CreateAlbumViewModel)
}