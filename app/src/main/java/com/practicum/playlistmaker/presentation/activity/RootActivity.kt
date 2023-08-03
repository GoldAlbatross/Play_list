package com.practicum.playlistmaker.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityRootBinding
import com.practicum.playlistmaker.presentation.viewmodel.RootViewModel
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
                R.id.search -> setInputMode(true)
                else -> {
                    setInputMode(false)
                    showBottomNav()
                }
            }
        }
    }

    private fun setInputMode(adjustNothing: Boolean) {
        if (adjustNothing) {
            this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        } else {
            this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
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