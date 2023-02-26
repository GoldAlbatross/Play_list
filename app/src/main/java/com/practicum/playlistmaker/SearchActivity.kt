package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.adapter.TrackAdapter
import com.practicum.playlistmaker.model.TrackResponse
import com.practicum.playlistmaker.okhttp.NetworkResponse
import com.practicum.playlistmaker.okhttp.TrackRetrofit
import kotlinx.android.parcel.Parcelize
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    companion object { const val KEY_STATE = "SearchActivity.KEY_STATE" }
    private lateinit var searchEditText: EditText
    private lateinit var clearingButton: ImageView
    private lateinit var dummy: View
    private lateinit var imgDummy: ImageView
    private lateinit var txtDummy: TextView
    private lateinit var btnDummy: Button
    private lateinit var toolbar: Toolbar

    // variables for save state of SearchActivity
    private lateinit var state: State
    private val callback = Runnable { renderState() }
    private val handler = Handler(Looper.getMainLooper())

    // variables for RecyclerView
    private lateinit var recycler: RecyclerView
    private val tracksAdapter = TrackAdapter()

    //variables for Retrofit
    private val retrofit = TrackRetrofit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //binds View
        toolbar = findViewById(R.id.toolbar)
        searchEditText = findViewById(R.id.edit_search)
        clearingButton = findViewById(R.id.icon_clear)
        recycler = findViewById(R.id.recycler)
        dummy = findViewById(R.id.dummy)
        imgDummy = findViewById(R.id.img_dummy)
        txtDummy = findViewById(R.id.txt_dummy)
        btnDummy = findViewById(R.id.btn_dummy)

        //come back
        toolbar.setOnClickListener { finish() }

        //handling a state
        state = savedInstanceState?.getParcelable(KEY_STATE) ?: State()
        searchEditText.setText(state.searchText)
        handler.postDelayed(callback, 500)

        //init the recyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = tracksAdapter
    }

    override fun onResume() {
        super.onResume()
        searchEditText.doOnTextChanged { text,_,_,_ ->
            clearingButton.visibility = clearButtonVisibility(text)
            state.searchText = "$text"
        }

        //clearing search field and recycler
        clearingButton.setOnClickListener {
            searchEditText.setText("")
            searchEditText.clearFocus()
            state.focus = false
            renderState()
            tracksAdapter.trackList.clear()
            tracksAdapter.notifyDataSetChanged()
        }

        //handling backendApi request/response
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                retrofit.listener = object : TrackRetrofit.TrackRetrofitListener {

                    override fun onSuccess(response: Response<TrackResponse>) {
                        if (response.isSuccessful){
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
            false
        }

        //refresh request
        btnDummy.setOnClickListener {
            retrofit.getResponseFromBackend(searchEditText.text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(callback)
        retrofit.listener = null
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

    private fun handlingSearchQuery(response: NetworkResponse) =
        when(response) {
            is NetworkResponse.Success -> {
                tracksAdapter.trackList.clear()
                tracksAdapter.trackList.addAll(response.listFromApi)
                tracksAdapter.notifyDataSetChanged()
                dummy.visibility = INVISIBLE
            }
            is NetworkResponse.NoData -> {
                tracksAdapter.trackList.clear()
                tracksAdapter.notifyDataSetChanged()
                dummy.visibility = VISIBLE
                imgDummy.background = AppCompatResources.getDrawable(this, R.drawable.search_dummy_empty)
                txtDummy.text = getString(R.string.empty_list)
                btnDummy.visibility = INVISIBLE
            }
            is NetworkResponse.Error-> {
                tracksAdapter.trackList.clear()
                tracksAdapter.notifyDataSetChanged()
                dummy.visibility = VISIBLE
                imgDummy.background = AppCompatResources.getDrawable(this, R.drawable.search_dummy_error)
                txtDummy.text = getString(R.string.error)
                btnDummy.visibility = VISIBLE
            }
        }
    @Parcelize
    class State: Parcelable {
        var searchText: String = ""
        var focus: Boolean = false
    }
}