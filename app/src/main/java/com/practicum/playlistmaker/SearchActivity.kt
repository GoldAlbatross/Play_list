package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val search = findViewById<EditText>(R.id.edit_search)
        val clearing = findViewById<ImageView>(R.id.icon_clear)
        search.doOnTextChanged { text,_,_,_ -> clearing.visibility = clearButtonVisibility(text) }
        clearing.setOnClickListener {
            search.text.clear()
            val hideKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            hideKeyboard?.hideSoftInputFromWindow(clearing.windowToken, 0)
            it.visibility = View.INVISIBLE
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) View.INVISIBLE
        else View.VISIBLE
    }
}