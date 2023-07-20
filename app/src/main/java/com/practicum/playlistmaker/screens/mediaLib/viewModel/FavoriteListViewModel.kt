package com.practicum.playlistmaker.screens.mediaLib.viewModel

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.features.storage.db_favorite.domain.api.FavoriteInteractor

class FavoriteListViewModel(
    private val favoriteInteractor: FavoriteInteractor
): ViewModel() {


}