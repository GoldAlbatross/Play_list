package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.screens.main.view_model.MainViewModel
import com.practicum.playlistmaker.screens.media_lib.view_model.FLViewModel
import com.practicum.playlistmaker.screens.media_lib.view_model.PLViewModel
import com.practicum.playlistmaker.screens.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.screens.search.view_model.SearchViewModel
import com.practicum.playlistmaker.screens.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.bind

val viewModelModule = module {
    viewModelOf(::MainViewModel).bind()
    viewModelOf(::PlayerViewModel).bind()
    viewModelOf(::SearchViewModel).bind()
    viewModelOf(::SettingsViewModel).bind()
    viewModelOf(::PLViewModel).bind()
    viewModelOf(::FLViewModel).bind()
}