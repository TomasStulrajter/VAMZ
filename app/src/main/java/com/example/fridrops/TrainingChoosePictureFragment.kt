package com.example.fridrops

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import kotlinx.android.synthetic.main.fragment_training_choose_picture.*
import kotlinx.android.synthetic.main.fragment_training_choose_word.*
import kotlinx.android.synthetic.main.fragment_training_translate_image.*
import kotlin.random.Random

/**
 * fragment predstavujuci ulohu, kde sa k slovu priraduje spravny obrazok - na vyber je zo 4 moznosti
 * @property correct_picture spravny obrazok, ktory ked uzivatel vyberie, tak odpovedal spravne
 * @property button_list zoznam vsetkych tlacidiel fragmentu
 * @property occupied_buttons zoznam tlacidiel, ktorym uz bol priradeny obrazok
 */
class TrainingChoosePictureFragment : Fragment() {

    private var correct_picture : String? = ""
    private val button_list = mutableListOf<ImageButton>()
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
        return inflater.inflate(R.layout.fragment_training_choose_picture, container, false)
    }

    /**
     * pouzita na priradenie obrazkov k tlacidlam
     */
    override fun onStart() {
        super.onStart()

        button_list.add(view_other_picture_1)
        button_list.add(view_other_picture_2)
        button_list.add(view_other_picture_3)
        button_list.add(view_other_picture_4)

        val correct_word = requireArguments().getString("correct_word")
        val correct_picture = requireArguments().getString("correct_picture")

        val other_picture_1 = requireArguments().getString("other_picture_1")
        val other_picture_2 = requireArguments().getString("other_picture_2")
        val other_picture_3 = requireArguments().getString("other_picture_3")

        this.correct_picture = correct_picture
        view_word_to_guess.text = correct_word

        //priradenie obrazkov tlacidlam
        mapPictureToButton(correct_picture)
        mapPictureToButton(other_picture_1)
        mapPictureToButton(other_picture_2)
        mapPictureToButton(other_picture_3)

        for (i in button_list.indices) {
            button_list[i].setOnClickListener { pictureClicked(button_list[i]) }
        }
    }

    /**
     * metoda priradena kazdemu tlacidlu - po stlaceni sa ohodnoti, ci bolo stlacene spravne tlacidlo a vysledok sa odosle treningovej aktivite
     * @param button tlacidlo, ktoremu sa priraduje tato metoda
     */
    private fun pictureClicked(button: ImageButton) {
        setFragmentResult("result_key", bundleOf("result" to (button.tag == correct_picture), "answer" to ""))
    }

    /**
     * namapuje obrazok na tlacidlo
     * @param picture URI daneho obrazku
     */
    private fun mapPictureToButton(picture: String?) {
        var index = Random.nextInt(4)
        while (occupied_buttons[index] != 0) {
            index = Random.nextInt(4)
        }
        button_list[index].setImageURI(Uri.parse(picture))
        button_list[index].tag = picture
        button_list[index].layoutParams.height = 400
        button_list[index].layoutParams.width = 400

        occupied_buttons[index] = 1
    }

}