package com.practicum.playlistmaker.settings.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.application.App
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener

class SettingsActivity : AppCompatActivity() {

    private val debouncer = Debouncer()
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(this, SettingsViewModel.factory())[SettingsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.sharing.debounceClickListener(debouncer) {
            viewModel.onClickedSharing()
        }
        binding.support.debounceClickListener(debouncer) {
            viewModel.onClickedSupport()
        }
        binding.agreement.debounceClickListener(debouncer) {
            viewModel.onClickedAgreement()
        }






        binding.nightSwtch.isChecked = App.instance.themeSwitcher.getBoolean()

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.nightSwtch.setOnCheckedChangeListener { _, isChecked ->
            App.instance.themeSwitcher.addBoolean(isChecked)
        }
    }
}