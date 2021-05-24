package com.example.fridrops

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * interface DAO - database access object - objekt, ktory zabezpecuje vstup a vystup z databazy pomocou queries
 */
@Dao
interface DictionaryDao {
    /**
     * vlozenie slova do databazy
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(word : Word)

    /**
     * vymazanie z databazy
     */
    @Delete
    suspend fun delete(word : Word)

    /**
     * uprava prvku v databaze
     */
    @Update
    suspend fun update(word : Word)

    /**
     * select vsetkych slov v databaze a zoradenie podla kategorie
     */
    @Query("SELECT * FROM dictionary ORDER BY category")
    fun getAll(): LiveData<List<Word>>

    /**
     * select vsetkych slov v databaze, ktore uz boli naucene
     */
    @Query("SELECT * FROM dictionary WHERE learned = 1")
    fun getAllLearned(): LiveData<List<Word>>

    /**
     * vyber slova z databazy podla atributu id
     */
    @Query("SELECT * FROM dictionary WHERE id = :id")
    fun getWordById(id: Int): LiveData<Word>

    /**
     * select poctu slov s databaze
     */
    @Query("SELECT COUNT(id) FROM dictionary")
    fun getWordCount(): LiveData<Int>

    /**
     * nastavi slovo z danym id ako naucene
     */
    @Query("UPDATE dictionary SET learned = 1 WHERE id = :id")
    fun setWordAsLearned(id: Int)

    /**
     * zmaze vsetky slova z databazy
     */
    @Query("DELETE FROM dictionary")
    fun deleteAllWords()

    /**
     * zvysi silu daneho slova
     */
    @Query("UPDATE dictionary SET strength = strength + 1 WHERE word = :word")
    fun increaseStrength(word: String?)
}
