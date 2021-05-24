package com.example.fridrops

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_dictionary.*
import kotlinx.android.synthetic.main.dictionary_item.*

/**
 * Aktivita, ktora pouziva recycer view na zobrazenie zoznamu vsetkych doposial naucenych slov
 * @property dictionaryViewModel instancia view modelu - implementovana ako singleton
 * @property adapter instancia adapteru pre recycler view
 * @property recyclerView instancia recycler viewu
 */
class DictionaryActivity : AppCompatActivity() {

    private lateinit var dictionaryViewModel: DictionaryViewModel
    private lateinit var adapter : DictionaryAdapter
    private lateinit var recyclerView: RecyclerView

    val resultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            Toast.makeText(this, "Result sent", Toast.LENGTH_SHORT)
            insertIntoDatabase(data!!.getStringExtra("word")!!, data!!.getStringExtra("translation")!!, data!!.getStringExtra("category")!!)
        } else {
            Toast.makeText(this, "Result not sent", Toast.LENGTH_SHORT)
        }
    }

    /**
     * override metody predka - inicializacia view modelu, recycler viewu a jeho adaptera a nastavenie observera pre zoznam vsetkych slov
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary)
        //setSupportActionBar(findViewById(R.id.toolbar))

        dictionaryViewModel = ViewModelProvider(this).get(DictionaryViewModel::class.java)

        adapter = DictionaryAdapter()
        recyclerView = findViewById<RecyclerView>(R.id.dictionary)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        dictionaryViewModel.dictionaryLearned.observe(this, Observer { words -> adapter.setWords(words) })
        buildDatabase()

    }

    /**
     * startuje aktivitu pridania noveho slova pomocou intentu
     */
    fun startAdd(view: View) {
        val dictionaryAddIntent = Intent(this, DictionaryAddActivity::class.java)
        resultContract.launch(dictionaryAddIntent)
    }

    /**
     * pridava do databazy nove slovo na zaklade dat vratenych z aktivity pridania noveho slova. Niektore parametre vyplna defaultnymi hodnotami
     * @param newWord text noveho slova
     * @param newTranslation preklad noveho slova
     * @param newCategory kategoria noveho slova
     */
    fun insertIntoDatabase(newWord: String, newTranslation: String, newCategory: String) {
        val newWordObject = Word(0, newWord, newTranslation, newCategory, 0, true, "android.resource://com.example.fridrops/drawable/placeholder", true)
        dictionaryViewModel.addWord(newWordObject)
    }

    /**
     * pociatocne naplnenie databazy - tato metoda sa zavola iba raz za celu dobu existencie aplikacie v zariadni
     */
    private fun buildDatabase() {
        val global_statistics = getSharedPreferences("global_statistics", Context.MODE_PRIVATE)
        var database_set = global_statistics.getBoolean("database_set", false)

        if (!database_set) {
            //animals
            dictionaryViewModel.addWord(Word(0, "cat", "mačka", "zvieratá", 0, false, "android.resource://com.example.fridrops/drawable/cat", false))
            dictionaryViewModel.addWord(Word(0, "cow", "krava", "zvieratá", 0, false, "android.resource://com.example.fridrops/drawable/cow", false))
            dictionaryViewModel.addWord(Word(0, "crocodile", "krokodíl", "zvieratá", 0, false, "android.resource://com.example.fridrops/drawable/crocodile", false))
            dictionaryViewModel.addWord(Word(0, "dog", "pes", "zvieratá", 0, false, "android.resource://com.example.fridrops/drawable/dog", false))
            dictionaryViewModel.addWord(Word(0, "frog", "žaba", "zvieratá", 0, false, "android.resource://com.example.fridrops/drawable/frog", false))
            dictionaryViewModel.addWord(Word(0, "monkey", "opica", "zvieratá", 0, false, "android.resource://com.example.fridrops/drawable/monkey", false))
            dictionaryViewModel.addWord(Word(0, "parrot", "papagáj", "zvieratá", 0, false, "android.resource://com.example.fridrops/drawable/parrot", false))
            dictionaryViewModel.addWord(Word(0, "rat", "potkan", "zvieratá", 0, false, "android.resource://com.example.fridrops/drawable/rat", false))
            dictionaryViewModel.addWord(Word(0, "shark", "žralok", "zvieratá", 0, false, "android.resource://com.example.fridrops/drawable/shark", false))
            dictionaryViewModel.addWord(Word(0, "spider", "pavúk", "zvieratá", 0, false, "android.resource://com.example.fridrops/drawable/spider", false))

            //food
            dictionaryViewModel.addWord(Word(0, "bread", "chlieb", "jedlo", 0, false, "android.resource://com.example.fridrops/drawable/bread", false))
            dictionaryViewModel.addWord(Word(0, "cheese", "syr", "jedlo", 0, false, "android.resource://com.example.fridrops/drawable/cheese", false))
            dictionaryViewModel.addWord(Word(0, "chocolate", "čokoláda", "jedlo", 0, false, "android.resource://com.example.fridrops/drawable/chocolate", false))
            dictionaryViewModel.addWord(Word(0, "fish", "ryba", "jedlo", 0, false, "android.resource://com.example.fridrops/drawable/fish", false))
            dictionaryViewModel.addWord(Word(0, "hazelnut", "lieskový orech", "jedlo", 0, false, "android.resource://com.example.fridrops/drawable/hazelnut", false))
            dictionaryViewModel.addWord(Word(0, "lemon", "citrón", "jedlo", 0, false, "android.resource://com.example.fridrops/drawable/lemon", false))
            dictionaryViewModel.addWord(Word(0, "pasta", "cestoviny", "jedlo", 0, false, "android.resource://com.example.fridrops/drawable/pasta", false))
            dictionaryViewModel.addWord(Word(0, "potato", "zemiak", "jedlo", 0, false, "android.resource://com.example.fridrops/drawable/potato", false))
            dictionaryViewModel.addWord(Word(0, "rice", "ryža", "jedlo", 0, false, "android.resource://com.example.fridrops/drawable/rice", false))
            dictionaryViewModel.addWord(Word(0, "tomato", "paradajka", "jedlo", 0, false, "android.resource://com.example.fridrops/drawable/tomato", false))

            database_set = true
            val editor = global_statistics.edit()
            editor.apply {
                putBoolean("database_set", database_set)
            }.apply()
        }
    }

    /**
     * pomocna metoda sluziaca na premazanie vsetkych slvo z databazy  - pomocou metody view modelu
     */
    fun deleteAllWords(view: View) {
        dictionaryViewModel.deleteAllWords()
    }
}