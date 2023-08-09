package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.presentation.viewmodel.AlbumViewModel
import com.practicum.playlistmaker.presentation.viewmodel.CreateAlbumViewModel
import com.practicum.playlistmaker.presentation.viewmodel.EditViewModel
import com.practicum.playlistmaker.presentation.viewmodel.RootViewModel
import com.practicum.playlistmaker.presentation.viewmodel.FavoriteListViewModel
import com.practicum.playlistmaker.presentation.viewmodel.PlayListViewModel
import com.practicum.playlistmaker.presentation.viewmodel.PlayerViewModel
import com.practicum.playlistmaker.presentation.viewmodel.SearchViewModel
import com.practicum.playlistmaker.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
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
    viewModelOf(::EditViewModel)
    viewModel { (id: Long) -> AlbumViewModel(albumId = id, playListInteractor = get()) }
}