package com.practicum.playlistmaker.settings.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.ui.router.SettingsRouter
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener

class SettingsActivity : AppCompatActivity() {

    private val debouncer = Debouncer()
    private val router by lazy { SettingsRouter(this) }
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this,
        SettingsViewModel.factory())[SettingsViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.themeSwitcherState().observe(this) {
            binding.nightSwtch.isChecked = it
        }

        binding.sharing.debounceClickListener(debouncer) {
            router.onClickedSharing()
        }

        binding.support.debounceClickListener(debouncer) {
            router.onClickedSupport()
        }

        binding.agreement.debounceClickListener(debouncer) {
            router.onClickedAgreement()
        }

        binding.toolbar.setNavigationOnClickListener {
            router.onClickedBack()
        }

        binding.nightSwtch.setOnCheckedChangeListener { _,night ->
            viewModel.changeTheme(night)
        }
    }
}