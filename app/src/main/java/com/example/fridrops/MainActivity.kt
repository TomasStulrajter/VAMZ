package com.example.fridrops

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

/**
 * aktivita sluziaca ako hlavne menu aplikacie
 */
class MainActivity : AppCompatActivity() {

    /**
     * zaciatok zivotneho cyklu
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main)
    }

    /**
     * metoda pre tlacidlo, ktora spusta trenovaciu aktivitu
     */
    fun startTraining(view: View) {
        val trainingIntent = Intent(this, TrainingActivity::class.java)
        startActivity(trainingIntent)
    }

    /**
     * metoda pre tlacidlo, ktora spusta slovnikovu aktivitu
     */
    fun startDictionary(view: View) {
        val dictionaryIntent = Intent(this, DictionaryActivity::class.java)
        startActivity(dictionaryIntent)
    }

    /**
     * metoda pre tlacidlo, ktora spusta aktivitu na zobrazenie statistik
     */
    fun startStatistics(view: View) {
        val statisticsIntent = Intent(this, StatisticsActivity::class.java)
        startActivity(statisticsIntent)
    }

    /**
     * metoda pre tlacidlo, ktora ukoncuje aplikaciu
     */
    fun startFinish(view: View) {
        finish()
        exitProcess(0)
    }

}