package com.example.fridrops

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.fragment.app.setFragmentResult
import kotlinx.android.synthetic.main.fragment_training_connect.*
import kotlin.random.Random

/**
 * fragment predstavujuci ulohu, kde sa vytvaraju dvojice - k slovu sa priraduje spravny obrazok
 */
class TrainingConnectFragment : Fragment() {

    /**
     * pomocne cislo pre prvy spravny par
     */
    var first_pair_sum = 0

    /**
     * pomocne cislo pre druhy spravny par
     */
    var second_pair_sum = 0

    /**
     * zoznam priznakov pre tlacidla, indikujuci, ktore uz boli stlacene
     */
    var buttons_clicked = arrayOf<Int>(0, 0, 0, 0)

    /**
     * pomocny list cisel pouzity pri kontrole spravnosti odopvedi pouzivatela
     */
    var button_values = mutableListOf<Int>()

    /**
     * priznak indikujuci, ze prvy par uz bol identifikovany
     */
    var first_pair_done = false

    /**
     * priznak indikujuci, ze druhy par uz bol identifikovany
     */
    var second_pair_done = false

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
        return inflater.inflate(R.layout.fragment_training_connect, container, false)
    }

    /**
     * pouzita na nastavenie listenerov pre jednotlive tlacidla fragmentu
     */
    override fun onStart() {
        super.onStart()
        //setCoordinates()
        setWordsAndPictures()

        word_1.setOnClickListener{
            word1Clicked()
        }

        word_2.setOnClickListener{
            word2Clicked()
        }

        picture_1.setOnClickListener{
            picture1Clicked()
        }

        picture_2.setOnClickListener{
            picture2Clicked()
        }
    }

    /**
     * metoda na nastavenie pozicie tlacidiel na obrazovke
     */
    private fun setCoordinates() {
        var x = Random.nextInt(0, 280)
        var y = Random.nextInt(0, 600)
        var param = word_1.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0,0,x ,y)

        x = Random.nextInt(0, 280)
        y = Random.nextInt(0, 600)
        param = word_2.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0,0,x ,y)

        x = Random.nextInt(0, 280)
        y = Random.nextInt(0, 600)
        param = picture_1.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0,0,x ,y)

        x = Random.nextInt(0, 280)
        y = Random.nextInt(0, 600)
        param = picture_2.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0,0,x ,y)
    }

    /**
     * nahodne priradenie slov a obrazokv jednotlivym tlacidlam fragmentu
     */
    private fun setWordsAndPictures() {
        word_1.text = requireArguments().getString("answer_1")
        word_2.text = requireArguments().getString("answer_2")

        picture_1.setImageURI(Uri.parse(requireArguments().getString("address_1")))
        picture_2.setImageURI(Uri.parse(requireArguments().getString("address_2")))
    }

    /**
     * listener pre tlacidlo z prvym slovom
     */
    private fun word1Clicked() {
        if (buttons_clicked[0] != 1) {
            buttons_clicked[0] = 1
            word_1.setBackgroundColor(Color.RED)
            button_values.add(1)
            checkPairs()
        }
    }

    /**
     * listener pre tlacidlo z druhym slovom
     */
    private fun word2Clicked() {
        if (buttons_clicked[1] != 1) {
            buttons_clicked[1] = 1
            word_2.setBackgroundColor(Color.RED)
            button_values.add(2)
            checkPairs()
        }
    }

    /**
     * listener pre tlacidlo z prvym obrazkom
     */
    private fun picture1Clicked() {
        if (buttons_clicked[2] != 1) {
            buttons_clicked[2] = 1
            picture_1.setBackgroundColor(Color.RED)
            button_values.add(1)
            checkPairs()
        }
    }

    /**
     * listener pre tlacidlo z druhym obrazkom
     */
    private fun picture2Clicked() {
        if (buttons_clicked[3] != 1) {
            buttons_clicked[3] = 1
            picture_2.setBackgroundColor(Color.RED)
            button_values.add(2)
            checkPairs()
        }
    }

    /**
     * po kazdej dvojici stlacenych tlacidiel vyhodnocuje, ci tvoria spravny par obrazok -> slovo
     */
    private fun checkPairs() {
        if (button_values.size == 2) {
            if (button_values.sum() == 2) {
                first_pair_done = true
                button_values.clear()
                word_1.visibility = View.GONE
                picture_1.visibility = View.GONE
            }
            if (button_values.sum() == 4){
                second_pair_done = true
                button_values.clear()
                word_2.visibility = View.GONE
                picture_2.visibility = View.GONE
            }
            if (first_pair_done && second_pair_done) {
                setFragmentResult("result_key", bundleOf("result" to true, "answer" to ""))
            }
            if (button_values.sum() == 3){
                setFragmentResult("result_key", bundleOf("result" to false, "answer" to ""))
            }
        }
    }

}