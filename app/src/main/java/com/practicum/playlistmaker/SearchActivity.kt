package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.adapter.TrackAdapter
import com.practicum.playlistmaker.model.Track
import kotlinx.android.parcel.Parcelize

class SearchActivity : AppCompatActivity() {
    companion object { const val KEY_STATE = "SearchActivity.KEY_STATE" }

    private lateinit var searchEditText: EditText
    private lateinit var clearingButton: ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var state: State
    private val callback = Runnable { renderState() }
    private val handler = Handler(Looper.getMainLooper())
    // variables for RecyclerView
    private lateinit var recycler: RecyclerView
    private val trackList = mutableListOf<Track>()

    @Parcelize
    class State: Parcelable {
        var searchText: String = ""
        var focus: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        toolbar = findViewById(R.id.toolbar)
        searchEditText = findViewById(R.id.edit_search)
        clearingButton = findViewById(R.id.icon_clear)

        toolbar.setOnClickListener { finish() }

        state = savedInstanceState?.getParcelable(KEY_STATE) ?: State()
        searchEditText.setText(state.searchText)
        handler.postDelayed(callback, 500)

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

        // handling RecyclerView
        initTrackList()
        setRecycler(trackList)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(callback)
    }

    private fun renderState() {
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

    private fun setRecycler(trackList: List<Track>) {
        recycler = findViewById(R.id.recycler)
        recycler.adapter = TrackAdapter(trackList)
    }

    private fun initTrackList() {
        trackList.add(Track(
            0,
            "Smells Like Teen Spirit",
            "Nirvana",
            "5:01",
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg")
        )
        trackList.add(Track(
            1,
            "Billie Jean",
            "Michael Jackson",
            "4:35",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg")
        )
        trackList.add(Track(
            2,
            "Staying' Alive",
            "Bee Gees",
            "4:10",
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg")
        )
        trackList.add(Track(
            3,
            "Whole Lotta Love",
            "Led Zeppelin",
            "5:33",
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg")
        )
        trackList.add(Track(
            4,
            "Sweet Child O'Mine",
            "Guns N' Roses",
            "5:03",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg")
        )
        (0..20).map { trackList.add(trackList[(0..4).random()]) }
    }
}