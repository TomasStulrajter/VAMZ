package com.example.fridrops

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * trieda zapuzdrujuca databazu a pomocou ktorej komunikuju z databazou ostatne triedy
 * @property dictionaryDao instancia objektu DAO
 * @property dictionary zoznam vsetkych slov
 * @property dictionaryLearned zoznam vsetkych naucenych slov
 * @property fetched_word slovo ziskane z databazy podla id
 * @property wordCount pocet slov v databaze
 */
class DictionaryViewModel(application: Application) : AndroidViewModel(application){

    private val dictionaryDao : DictionaryDao
    val dictionary : LiveData<List<Word>>
    val dictionaryLearned : LiveData<List<Word>>
    private lateinit var fetched_word: LiveData<Word>
    val wordCount : LiveData<Int>
    init {
        val database : DictionaryDatabase = DictionaryDatabase.getDatabase(application)
        dictionaryDao = database.dictionaryDao()
        dictionary = dictionaryDao.getAll()
        wordCount = dictionaryDao.getWordCount()

        dictionaryLearned = dictionaryDao.getAllLearned()
    }

    /**
     * vlozenie slova do databazy
     */
    fun addWord(word : Word) {
        viewModelScope.launch(Dispatchers.IO) {
            dictionaryDao.insert(word)
        }
    }

    /**
     * vymazanie slova z databazy
     */
    fun removeWord(word : Word) {
        viewModelScope.launch(Dispatchers.IO) {
            dictionaryDao.delete(word)
        }
    }

    /**
     * uprava prvku v databaze
     */
    fun updateWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            dictionaryDao.update(word)
        }
    }

    /**
     * vyber slova z databazy podla atributu id
     */
    fun getWordById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            fetched_word = dictionaryDao.getWordById(id)
        }
    }

    /**
     * nastavi slovo z danym id ako naucene
     */
    fun setWordAsLearned(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dictionaryDao.setWordAsLearned(id)
        }
    }

    /**
     * zmaze vsetky slova z databazy
     */
    fun deleteAllWords() {
        viewModelScope.launch(Dispatchers.IO) {
            dictionaryDao.deleteAllWords()
        }
    }

    /**
     * zvysi silu daneho slova
     */
    fun increaseStrength(word: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            dictionaryDao.increaseStrength(word)
        }
    }
}