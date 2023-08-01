package com.practicum.playlistmaker.screens.root.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityRootBinding
import com.practicum.playlistmaker.screens.root.viewModel.RootViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity() {

    private val viewModel by viewModel<RootViewModel>()
    private var viewBinding: ActivityRootBinding? = null
    private val binding get() = viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.setTheme()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.containerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.menu.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.createFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun hideBottomNav() {
        binding.menu.visibility = View.GONE
    }

    private fun showBottomNav() {
        binding.menu.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }
}