package com.example.fridrops

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import kotlinx.android.synthetic.main.fragment_training_choose_word.*
import kotlinx.android.synthetic.main.fragment_training_translate_image.*
import kotlin.random.Random

/**
 * fragment predstavujuci ulohu, kde sa k obrazku priraduje spravne slovo - na vyber je zo 4 moznosti
 * @property correct_word spravne slovo, ktore ked uzivatel vyberie, tak odpovedal spravne
 * @property button_list zoznam vsetkych tlacidiel fragmentu
 * @property occupied_buttons zoznam tlacidiel, ktorym uz bol priradeny obrazok
 */
class TrainingChooseWordFragment : Fragment() {

    private var correct_word : String? = ""
    private val button_list = mutableListOf<Button>()
    private var occupied_buttons = arrayOf<Int>(0, 0, 0, 0)

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
        return inflater.inflate(R.layout.fragment_training_choose_word, container, false)
    }

    /**
     * pouzita na priradenie slov k tlacidlam
     */
    override fun onStart() {
        super.onStart()

        button_list.add(view_other_word_1)
        button_list.add(view_other_word_2)
        button_list.add(view_other_word_3)
        button_list.add(view_other_word_4)

        val correct_word = requireArguments().getString("correct_word")
        val correct_picture = requireArguments().getString("correct_picture")

        val other_word_1 = requireArguments().getString("other_word_1")
        val other_word_2 = requireArguments().getString("other_word_2")
        val other_word_3 = requireArguments().getString("other_word_3")

        this.correct_word = correct_word

        //priradenie slov tlacidlam
        mapWordToButton(correct_word)
        mapWordToButton(other_word_1)
        mapWordToButton(other_word_2)
        mapWordToButton(other_word_3)

        view_picture_to_guess.setImageURI(Uri.parse(correct_picture))
        view_picture_to_guess.layoutParams.height = 400
        view_picture_to_guess.layoutParams.width = 400

        for (i in button_list.indices) {
            button_list[i].setOnClickListener { wordClicked(button_list[i]) }
        }
    }

    /**
     * metoda priradena kazdemu tlacidlu - po stlaceni sa ohodnoti, ci bolo stlacene spravne tlacidlo a vysledok sa odosle treningovej aktivite
     * @param button tlacidlo. ktoremu sa priraduje tato metoda
     */
    private fun wordClicked(button: Button) {
        setFragmentResult("result_key", bundleOf("result" to (button.text == correct_word), "answer" to correct_word))
    }

    /**
     * namapuje slovo na tlacidlo
     * @param word dane slovo
     */
    private fun mapWordToButton(word: String?) {
        var index = Random.nextInt(4)
        while (occupied_buttons[index] != 0) {
            index = Random.nextInt(4)
        }
        button_list[index].text = word
        occupied_buttons[index] = 1
    }

}