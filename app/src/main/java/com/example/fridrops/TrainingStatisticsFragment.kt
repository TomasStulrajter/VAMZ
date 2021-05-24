package com.example.fridrops

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import kotlin.math.roundToInt

/**
 * fragment zobrazujuci vysledok treningu - statistiku riesenych uloh a uspesnosti - pomocou dialogu
 */
class TrainingStatisticsFragment : DialogFragment() {

    /**
     * override metody predka
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * zaciatok zivotneho cyklu
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_training_statistics, container, false)
    }

    /**
     * vytvori dialog zobrazujuci statistiky treningu
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val completed_tasks = requireArguments().getInt("completed_tasks")
        val correct_tasks = requireArguments().getInt("correct_tasks")
        var success_ratio = 0
        if(completed_tasks != 0) {
            success_ratio = (correct_tasks.toDouble() / completed_tasks.toDouble() * 100).roundToInt()
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.dialogStyle)
            builder.setTitle(R.string.training_result)
            builder.setMessage("Riešených úloh : $completed_tasks \n" +
                    "Splnených úloh : $correct_tasks \n" +
                    "Percentuálna úspešnosť : $success_ratio%")

            builder.setNeutralButton("Ok") { _, _ ->
                setFragmentResult("statistics_key", bundleOf("Confirmed" to true))
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}