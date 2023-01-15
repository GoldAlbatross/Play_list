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
import android.widget.TextView
import androidx.core.widget.doOnTextChanged

class SearchActivity : AppCompatActivity() {

    private lateinit var searchText: String
    private lateinit var search: EditText
    companion object {
        const val KEY_EDIT_TEXT = "SearchActivity.KEY_EDIT_TEXT"
        const val KEY_FOCUS = "SearchActivity.KEY_FOCUS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        search = findViewById(R.id.edit_search)
        val clearing = findViewById<ImageView>(R.id.icon_clear)
        val button = findViewById<Button>(R.id.button)

        // возвращаю на главный экран.
        button.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }

        // делаю видимым clearing, сохраняю текст в глобальную переменную.
        search.doOnTextChanged { text,_,_,_ ->
            clearing.visibility = clearButtonVisibility(text)
            searchText = "$text"
        }

        //очищаю поле, убираю клавиатуру, делаю невидимым clearing.
        clearing.setOnClickListener {
            search.text.clear()
            val hideKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            hideKeyboard?.hideSoftInputFromWindow(search.windowToken, 0)
            it.visibility = View.INVISIBLE
        }

        // восстанавливаю состояние клавиатуры до переворота.
        if (savedInstanceState != null) {
            search.setText(savedInstanceState.getString(KEY_EDIT_TEXT))
            if (savedInstanceState.getBoolean(KEY_FOCUS)) { search.requestFocus() }
        }

    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) View.INVISIBLE
        else View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        /* Если я обращусь к объекту search: EditText, так будет не правильно?
        outState.putString(KEY_EDIT_TEXT, "${search.text}")*/
        outState.putString(KEY_EDIT_TEXT, searchText)
        // сохраняю состояние клавиатуры.
        if (this.currentFocus == search) {
            outState.putBoolean(KEY_FOCUS, true) }
        else outState.putBoolean(KEY_FOCUS, false)
    }
}