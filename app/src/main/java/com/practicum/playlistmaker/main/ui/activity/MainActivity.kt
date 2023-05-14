package com.practicum.playlistmaker.main.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.main.ui.router.MainRouter
import com.practicum.playlistmaker.main.ui.view_model.MainViewModel
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener

class MainActivity : AppCompatActivity() {

    private val debouncer = Debouncer()
    private val router by lazy { MainRouter(this) }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this,
            MainViewModel.factory())[MainViewModel::class.java]

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