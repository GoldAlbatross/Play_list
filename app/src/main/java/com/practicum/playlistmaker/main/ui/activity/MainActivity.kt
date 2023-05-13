package com.practicum.playlistmaker.main.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.main.ui.view_model.MainViewModel
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener

class MainActivity : AppCompatActivity() {

    private val debouncer = Debouncer()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(this, MainViewModel.factory())[MainViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.search.debounceClickListener(debouncer) {
            viewModel.onClickedSearch()
        }

        binding.media.debounceClickListener(debouncer) {
            viewModel.onClickedMediaLib()
        }

        binding.settings.debounceClickListener(debouncer) {
            viewModel.onClickedSettings()
        }
    }
}