package com.example.fridrops

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_training_result.*

/**
 * fragment zobrazujuci vysledok ulohy - ci bola uspesne splnena alebo nie
 */
class TrainingResultFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_training_result, container, false)
    }

    /**
     * pouzita na zobrazenie spravy o spravnosti vysledku
     */
    override fun onStart() {
        super.onStart()
        showResultMessage()
    }

    /**
     * zobrazenie spravy o spravnosti vysledku a prehratie korespondujuceho zvukoveho efektu
     */
    private fun showResultMessage() {
        val result = requireArguments().getBoolean("result")
        val answer = requireArguments().getString("answer")

        if (result) {
            result_message.text = "Správne!"
            playSound(1)
        } else {
            if (answer == "") {
                result_message.text = "Nesprávne!"
                playSound(0)
            }
            else {
                result_message.text = "Nesprávne! Správna odpoveď bola '$answer'"
                playSound(0)
            }
        }
    }

    /**
     * prehratie zvukoveho efektu podla vysledku ulohy
     */
    fun playSound(occasion: Int) {
        var soundPlayer : MediaPlayer
        if (occasion == 0) {
            soundPlayer = MediaPlayer.create(requireContext(), R.raw.failure)
            soundPlayer.start()
        } else if (occasion == 1) {
            soundPlayer = MediaPlayer.create(requireContext(), R.raw.success)
            soundPlayer.start()
        }
    }

}