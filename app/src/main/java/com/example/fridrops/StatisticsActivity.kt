package com.example.fridrops

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlin.math.roundToInt

/**
 * aktivita sluziaca na zobrazovanie celkovych statistik pouzivatela
 */
class StatisticsActivity : AppCompatActivity() {
    /**
     * zaciatok zivotneho cyklu
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
    }

    /**
     * override metody predka
     */
    override fun onStart() {
        super.onStart()
        setStatistics()
        reset.setOnClickListener { resetStatistics() }
    }

    /**
     * vytiahnutie statistik z globalnych shared preferences a ich priradenie do text viewov
     */
    private fun setStatistics() {
        val global_statistics = getSharedPreferences("global_statistics", Context.MODE_PRIVATE)

        val saved_completed_tasks = global_statistics.getInt("completed_tasks", 0)
        val saved_correct_tasks = global_statistics.getInt("correct_tasks", 0)
        var success_ratio = 0
        if (saved_completed_tasks != 0) {
            success_ratio = (saved_correct_tasks.toDouble() / saved_completed_tasks.toDouble() * 100).roundToInt()
        }
        var session_count = global_statistics.getInt("session_count", 0)

        val text_completed_tasks = "Celkový počet riešených úloh : ${saved_completed_tasks.toString()}"
        val text_correct_tasks = "Správne vyriešených úloh : ${saved_correct_tasks.toString()}"
        val text_success_ratio = "Celková úspešnosť : ${success_ratio.toString()}%"
        val text_session_count = "Pocet ukoncenych treningov : ${session_count.toString()}"
        view_completed_tasks.text = text_completed_tasks
        view_correct_tasks.text = text_correct_tasks
        view_success_ratio.text = text_success_ratio
        view_session_count.text = text_session_count
    }

    /**
     * premazanie vsetkych globalnych statistik
     */
    private fun resetStatistics() {
        val global_statistics = getSharedPreferences("global_statistics", Context.MODE_PRIVATE)

        var saved_completed_tasks = global_statistics.getInt("completed_tasks", 0)
        var saved_correct_tasks = global_statistics.getInt("correct_tasks", 0)
        var saved_session_count = global_statistics.getInt("session_count", 0)
        saved_completed_tasks = 0
        saved_correct_tasks = 0
        saved_session_count = 0

        val editor = global_statistics.edit()
        editor.apply {
            putInt("completed_tasks", saved_completed_tasks)
            putInt("correct_tasks", saved_correct_tasks)
            putInt("session_count", saved_session_count)
        }.apply()

        val text_completed_tasks = "${R.string.total_completed_words} : ${saved_completed_tasks.toString()}"
        val text_correct_tasks = "${R.string.total_correct_words} : ${saved_correct_tasks.toString()}"
        val text_success_ratio = "${R.string.total_success_ratio} : ${0}%"
        val text_session_count = "${R.string.total_session_count} : ${0}"
        view_completed_tasks.text = text_completed_tasks
        view_correct_tasks.text = text_correct_tasks
        view_success_ratio.text = text_success_ratio
        view_session_count.text = text_session_count
    }

}