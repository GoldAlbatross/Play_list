package com.practicum.playlistmaker.presentation.activity

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.Logger
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityRootBinding
import com.practicum.playlistmaker.presentation.viewmodel.RootViewModel
import com.practicum.playlistmaker.utils.className
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity() {

    private val viewModel by viewModel<RootViewModel>()
    private var viewBinding: ActivityRootBinding? = null
    private val logger: Logger by inject()
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
            logger.log("$className -> addOnDestinationChangedListener { destination = ${destination.label} }")
            when (destination.id) {
                R.id.createFragment -> hideBottomNav()
                R.id.albumFragment -> hideBottomNav()
                R.id.editFragment -> hideBottomNav()
                R.id.searchFragment -> setInputMode(true)
                else -> {
                    setInputMode(false)
                    showBottomNav()
                }
            }
        }
    }

    private fun setInputMode(adjustNothing: Boolean) {
        logger.log("$className -> setInputMode(adjustNothing: $adjustNothing)")
        if (adjustNothing) {
            this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        } else {
            this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    private fun hideBottomNav() {
        logger.log("$className -> hideBottomNav()")
        binding.menu.visibility = View.GONE
    }

    private fun showBottomNav() {
        logger.log("$className -> showBottomNav()")
        binding.menu.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.log("$className -> onDestroy()")
        viewBinding = null
    }
}