package com.practicum.playlistmaker.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.app.TAG
import com.practicum.playlistmaker.databinding.ActivityRootBinding
import com.practicum.playlistmaker.presentation.viewmodel.RootViewModel
import com.practicum.playlistmaker.utils.simpleName
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
            Log.d(TAG, "RootActivity -> navController.addOnDestinationChangedListener { destination = ${destination.label} }")
            when (destination.id) {
                R.id.createFragment -> hideBottomNav()
                R.id.albumFragment -> hideBottomNav()
                R.id.search -> setInputMode(true)
                else -> {
                    setInputMode(false)
                    showBottomNav()
                }
            }
        }
    }

    private fun setInputMode(adjustNothing: Boolean) {
        Log.d(TAG, "RootActivity -> setInputMode(adjustNothing: $adjustNothing)")
        if (adjustNothing) {
            this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        } else {
            this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    private fun hideBottomNav() {
        Log.d(TAG, "RootActivity -> hideBottomNav()")
        binding.menu.visibility = View.GONE
    }

    private fun showBottomNav() {
        Log.d(TAG, "RootActivity -> showBottomNav()")
        binding.menu.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "RootActivity -> onDestroy()")
        viewBinding = null
    }
}