package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.adapter.SwipeHandlerCallback
import com.practicum.playlistmaker.adapter.TrackAdapter
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.model.TrackResponse
import com.practicum.playlistmaker.okhttp.NetworkResponse
import com.practicum.playlistmaker.okhttp.TrackRetrofit
import com.practicum.playlistmaker.okhttp.TrackRetrofitListener
import com.practicum.playlistmaker.tools.DELAY_1000
import com.practicum.playlistmaker.tools.DELAY_500
import com.practicum.playlistmaker.tools.getParcelableFromBundle
import kotlinx.parcelize.Parcelize
import retrofit2.Response

class SearchActivity : AppCompatActivity(R.layout.activity_search) {
    companion object {
        const val KEY_STATE = "searchActivity_state"
        const val KEY_TRACK = "searchActivity_track"
    }

    private lateinit var searchEditText: EditText
    private lateinit var clearingButton: ImageView
    private lateinit var dummy: View
    private lateinit var imgDummy: ImageView
    private lateinit var txtDummy: TextView
    private lateinit var header: TextView
    private lateinit var btnDummy: Button
    private lateinit var progress: ProgressBar
    private val requestDataRunnable = Runnable { requestData() }

    // variables for save state of SearchActivity
    private lateinit var state: State
    private val callback = Runnable { renderState() }
    private val handler = Handler(Looper.getMainLooper())

    // variables for RecyclerView
    private lateinit var recycler: RecyclerView
    private val trackAdapter = TrackAdapter()
    private lateinit var footer: Button

    //variables for Retrofit
    private val retrofit = TrackRetrofit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binds View
        searchEditText = findViewById(R.id.edit_search)
        clearingButton = findViewById(R.id.icon_clear)
        recycler = findViewById(R.id.recycler)
        dummy = findViewById(R.id.dummy)
        imgDummy = findViewById(R.id.img_dummy)
        txtDummy = findViewById(R.id.txt_dummy)
        btnDummy = findViewById(R.id.btn_dummy)
        footer = findViewById(R.id.btn_clear_history)
        header = findViewById(R.id.txt_history)
        progress = findViewById(R.id.progress_bar)

        //handling a state
        state = savedInstanceState?.getParcelableFromBundle(KEY_STATE, State::class.java) ?: State()
        searchEditText.setText(state.searchText)
        handler.postDelayed(callback, DELAY_500)
        clearingButton.visibility = state.cross

        // init the recyclerView
        trackAdapter.trackList.addAll(App.instance.trackStorage.getTracks())
        recycler.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = trackAdapter
        }

        // Handling a swipe or drag
        val swipeHandler = SwipeHandlerCallback(this, trackAdapter)
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycler)

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()

        if (recycler.adapter != null) recycler.adapter?.notifyDataSetChanged()

        // hide the cross button
        searchEditText.doOnTextChanged { text, _, _, _ ->
            if (searchEditText.hasFocus() && text?.isNotEmpty() == true) {
                requestDataDebounce()
                showEmptyList()
            }
            else
                showHistorySearch(searchEditText.hasFocus())
            btnVisibility(text, clearingButton)
        }

        // catch the focus and set visibility
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (searchEditText.text.isNullOrEmpty()) showHistorySearch(hasFocus)
        }

        // clearing search field and recycler
        clearingButton.setOnClickListener {
            searchEditText.setText("")
            searchEditText.clearFocus()
            state.focus = false
            renderState()
        }


        // refresh request
        btnDummy.setOnClickListener {
            retrofit.getResponseFromBackend(searchEditText.text.toString())
        }

        footer.setOnClickListener {
            App.instance.trackStorage.clearTrackList()
            showEmptyList()
        }

        trackAdapter.listener = { track ->
            App.instance.trackStorage.addTrack(track)
            startActivity(Intent(this, PlayerActivity::class.java).putExtra(KEY_TRACK, track))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        state.searchText = searchEditText.text.toString()
        state.focus = searchEditText.hasFocus()
        state.cross = clearingButton.visibility
        outState.putParcelable(KEY_STATE, state)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(callback)
        retrofit.listener = null
    }

    private fun requestData () {
        retrofit.listener = object : TrackRetrofitListener {

            override fun onSuccess(response: Response<TrackResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.trackList.isNotEmpty())
                        handlingSearchQuery(NetworkResponse.Success(response.body()?.trackList!!))
                    else
                        handlingSearchQuery(NetworkResponse.NoData)
                }
            }

            override fun onError(t: Throwable) =
                handlingSearchQuery(NetworkResponse.Error(t.toString()))
        }
        retrofit.getResponseFromBackend(searchEditText.text.toString())
    }

    private fun renderState() {
        if (state.focus) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchEditText, 0)
        } else {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }
    }

    private fun btnVisibility(text: CharSequence?, view: View) {
        view.visibility = if (text.isNullOrEmpty()) INVISIBLE else VISIBLE
    }

    private fun showEmptyList() {
        recycler.visibility = INVISIBLE
        header.visibility = GONE
        footer.visibility = GONE
    }

    private fun showHistorySearch(hasFocus: Boolean) {
        if (hasFocus && trackAdapter.trackList.isNotEmpty()) {
            recycler.visibility = VISIBLE
            header.visibility = VISIBLE
            footer.visibility = VISIBLE
            setItems(App.instance.trackStorage.getTracks())
        } else showEmptyList()
    }

    private fun handlingSearchQuery(response: NetworkResponse) {
        progress.visibility = GONE
        recycler.visibility = VISIBLE
        when (response) {
            is NetworkResponse.Success -> {
                setItems(response.listFromApi)
                dummy.visibility = GONE
            }
            is NetworkResponse.NoData -> {
                trackAdapter.trackList.clear()
                dummy.visibility = VISIBLE
                imgDummy.background = setImage(R.drawable.search_dummy_empty)
                txtDummy.text = getString(R.string.empty_list)
                btnDummy.visibility = INVISIBLE
            }
            is NetworkResponse.Error -> {
                trackAdapter.trackList.clear()
                dummy.visibility = VISIBLE
                imgDummy.background = setImage(R.drawable.search_dummy_error)
                txtDummy.text = getString(R.string.error)
                btnDummy.visibility = VISIBLE
            }
        }
    }

    private fun requestDataDebounce() {
        progress.visibility = VISIBLE
        handler.removeCallbacks(requestDataRunnable)
        handler.postDelayed(requestDataRunnable, DELAY_1000)
    }

    private fun setImage(image: Int) =
        AppCompatResources.getDrawable(this, image)

    private fun setItems(items: List<Track>?) {
        trackAdapter.trackList.clear()
        if (items != null) trackAdapter.trackList.addAll(items)
        trackAdapter.notifyDataSetChanged()
    }

    @Parcelize
    class State(
        var searchText: String = "",
        var focus: Boolean = false,
        var cross: Int = GONE,
    ) : Parcelable
}