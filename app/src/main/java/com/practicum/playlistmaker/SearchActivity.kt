package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import kotlinx.android.parcel.Parcelize

class SearchActivity : AppCompatActivity() {

    companion object { const val KEY_STATE = "SearchActivity.KEY_STATE" }

    private lateinit var searchEditText: EditText
    private lateinit var clearingButton: ImageView
    private lateinit var backButton: Button
    private lateinit var state: State

    @Parcelize
    class State: Parcelable {
        var searchText: String = ""
        var focus: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        backButton = findViewById(R.id.button)
        searchEditText = findViewById(R.id.edit_search)
        clearingButton = findViewById(R.id.icon_clear)

        backButton.setOnClickListener { finish() }

        state = savedInstanceState?.getParcelable(KEY_STATE) ?: State()
        renderState()

        searchEditText.doOnTextChanged { text,_,_,_ ->
            clearingButton.visibility = clearButtonVisibility(text)
            state.searchText = "$text"
        }

        clearingButton.setOnClickListener {
            searchEditText.setText("")
            searchEditText.clearFocus()
            state.focus = false
            renderState()
        }
    }


    private fun renderState() {
        searchEditText.setText(state.searchText)
        if (state.focus) { val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchEditText, 0)
        }
        else { val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) INVISIBLE
        else VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (currentFocus == searchEditText) state.focus = true
        outState.putParcelable(KEY_STATE, state)
    }
}