package com.example.fridrops

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

/**
 * aktivita sluziaca na pridavanie noveho slova do databazy
 * @property dictionaryViewModel instancia view modelu - implementovana ako singleton
 */
class DictionaryAddActivity : AppCompatActivity() {

    private lateinit var dictionaryViewModel: DictionaryViewModel

    /**
     * zaciatok zivotneho cyklu - inicializacia view modelu
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary_add)
        //setSupportActionBar(findViewById(R.id.toolbar))

        dictionaryViewModel = ViewModelProvider(this).get(DictionaryViewModel::class.java)
    }

    /**
     * odosle data vypisane v poliach textviewov slovnikovej aktivite, ktora ich nasledne ulozi do databazy
     */
    fun sendData(view: View) {
        val newWord = findViewById<EditText>(R.id.add_word).text.toString()
        val newTranslation = findViewById<EditText>(R.id.add_translation).text.toString()
        val newCategory = findViewById<EditText>(R.id.add_category).text.toString()

        if (checkInput(newWord, newTranslation, newCategory)) {
            Toast.makeText(this, R.string.nove_slovo_pridane, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.nove_slovo_nepridane, Toast.LENGTH_SHORT).show()
        }

        val dictionaryIntent = Intent(this, DictionaryActivity::class.java)
        dictionaryIntent.putExtra("word", newWord)
        dictionaryIntent.putExtra("translation", newTranslation)
        dictionaryIntent.putExtra("category", newCategory)
        setResult(Activity.RESULT_OK, dictionaryIntent)
        finish()
    }

    /**
     * skontroluje vsutp od pouzivatela
     * @param word nove slovo zadane pouzivatelom
     * @param translation novy preklad zadany pouzivatelom
     * @param category nova kategoria zadana pouzivatelom
     */
    private fun checkInput(word: String, translation: String, category: String): Boolean {
        return !(TextUtils.isEmpty(word) || TextUtils.isEmpty(translation) || TextUtils.isEmpty(category))
    }

    /**
     * definuje co sa stane ak uzivatel 'vycuva' z aktivity pomocou tlacidla 'spat' - aktivita sa dokonci
     */
    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}