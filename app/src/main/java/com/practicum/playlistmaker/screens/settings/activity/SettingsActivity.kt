package com.practicum.playlistmaker.screens.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.screens.settings.router.SettingsRouter
import com.practicum.playlistmaker.screens.settings.view_model.SettingsViewModel
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val debouncer = Debouncer()
    private val router by lazy { SettingsRouter(this) }
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private val viewModel: SettingsViewModel by viewModel()

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

        binding.backBtn.setNavigationOnClickListener {
            router.onClickedBack()
        }

        binding.nightSwtch.setOnCheckedChangeListener { _,night ->
            viewModel.changeTheme(night)
        }
    }
}