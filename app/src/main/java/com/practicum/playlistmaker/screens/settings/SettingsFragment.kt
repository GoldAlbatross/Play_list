package com.practicum.playlistmaker.screens.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.screens.settings.router.SettingsRouter
import com.practicum.playlistmaker.screens.settings.viewModel.SettingsViewModel
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener
import com.practicum.playlistmaker.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.absoluteValue

class SettingsFragment: Fragment(R.layout.fragment_settings) {

    private val debouncer = Debouncer()
    private val router by lazy { SettingsRouter(requireContext()) }
    private val viewModel: SettingsViewModel by viewModel()
    private val binding by viewBinding<FragmentSettingsBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.themeSwitcherState().observe(viewLifecycleOwner) {
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

        binding.nightSwtch.setOnCheckedChangeListener { _,night ->
            viewModel.changeTheme(night)
        }
    }
}