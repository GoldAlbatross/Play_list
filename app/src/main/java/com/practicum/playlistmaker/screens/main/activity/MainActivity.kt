package com.practicum.playlistmaker.screens.main.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.screens.main.router.MainRouter
import com.practicum.playlistmaker.screens.main.view_model.MainViewModel
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val debouncer = Debouncer()
    private val router by lazy { MainRouter(this) }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.setTheme()

        binding.search.debounceClickListener(debouncer) {
            router.onClickedSearch()
        }

        binding.media.debounceClickListener(debouncer) {
            router.onClickedMediaLib()
        }

        binding.settings.debounceClickListener(debouncer) {
            router.onClickedSettings()
        }
    }
}