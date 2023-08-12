package com.practicum.playlistmaker.presentation.fragment.create

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.app.TAG
import com.practicum.playlistmaker.databinding.CreateFragmentBinding
import com.practicum.playlistmaker.domain.local_db.model.Album
import com.practicum.playlistmaker.presentation.activity.RootActivity
import com.practicum.playlistmaker.presentation.viewmodel.CreateAlbumViewModel
import com.practicum.playlistmaker.utils.KEY_TRACK
import com.practicum.playlistmaker.utils.className
import com.practicum.playlistmaker.utils.getParcelableFromBundle
import com.practicum.playlistmaker.utils.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
open class CreateFragment: Fragment(R.layout.create_fragment) {

    protected open val binding by viewBinding<CreateFragmentBinding>()
    protected open val viewModel by viewModel<CreateAlbumViewModel>()
    protected val album by lazy { arguments?.getParcelableFromBundle(KEY_TRACK, Album::class.java) }
    private var dialog: AlertDialog? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "${className()} -> onViewCreated()")
        super.onViewCreated(view, savedInstanceState)

        prepareDialog()
        registerImagePicker()
        setListeners()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                Log.d(TAG, "${className()} -> uiState.collect { state = ${state.className()} }")
                when (state) {
                    is ScreenState.Default -> {  }
                    is ScreenState.Button -> { createButtonClickable(state.clickable) }
                    is ScreenState.GoBack -> { goBack() }
                    is ScreenState.SaveAlbum -> { saveAlbum(state.name) }
                    is ScreenState.Dialog -> { dialog?.show() }
                }
            }
        }
    }

    private fun setListeners() {
        Log.d(TAG, "${className()} -> setListeners()")
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            viewModel.onBackPressed()
        }
        binding.apply {
            create.setOnClickListener { viewModel.onCreatePressed(album) }
            toolbar.setNavigationOnClickListener {
                viewModel.onBackPressed()
            }
            name.doOnTextChanged { text,_,_,_ ->
                viewModel.onNameTextChange(text.toString())
            }
            description.doOnTextChanged { text,_,_,_ ->
                viewModel.onDescriptionTextChange(text.toString())
            }
        }
    }

    private fun prepareDialog() {
        Log.d(TAG, "${className()} -> prepareDialog()")
        dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.queryTitle)
            .setMessage(R.string.data_will_be_lost)
            .setIcon(R.drawable.dangerous)
            .setNegativeButton(R.string.cancel) { _,_ -> viewModel.dialogIsHide() }
            .setPositiveButton(R.string.finish) { _,_ -> findNavController().navigateUp() }
            .create()
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
    }


    private fun registerImagePicker() {
        Log.d(TAG, "${className()} -> registerImagePicker()")
        val picker =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                uri?.let {
                    Glide.with(this)
                        .load(it)
                        .transform(RoundedCorners(8))
                        .into(binding.frameImage)
                    viewModel.setUri(uri.toString())
                }
            }
        binding.frameImage.setOnClickListener {
            picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun saveAlbum(album: String) {
        Log.d(TAG, "${className()} -> saveAlbum(album: $album)")
        Snackbar
            .make(requireContext(), binding.root, getString(R.string.album_created, album), Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .show()
        goBack()
    }

    private fun goBack() {
        Log.d(TAG, "${className()} -> goBack()")
        if (requireActivity() is RootActivity) {
            findNavController().navigateUp()
        } else {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun createButtonClickable(state: Boolean) {
        Log.d(TAG, "${className()} -> createButtonClickable(state: $state)")
        binding.create.isEnabled = state
    }

    companion object { fun newInstance() = CreateFragment() }

}